<?php
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

/**
 * Format = atom output converter, for format definition see:
 * http://sites.google.com/a/opensocial.org/opensocial/Technical-Resources/opensocial-spec-v08/restful-api-specification
 */
class OutputAtomConverter extends OutputConverter {
	private static $nameSpace = 'http://www.w3.org/2005/Atom';
	private static $osNameSpace = 'http://ns.opensocial.org/2008/opensocial';
	private static $xmlVersion = '1.0';
	private static $charSet = 'UTF-8';
	private static $formatOutput = true;
	//FIXME osearch fields break the validator ... remove option once i know if they should be included or not
	private static $includeOsearch = false;
	// this maps the REST url to the atom content type
	private static $entryTypes = array('people' => 'person', 'appdata' => 'appdata', 
			'activities' => 'activity', 'messages' => 'messages');
	private $doc;

	function outputResponse(ResponseItem $responseItem, RestRequestItem $requestItem)
	{
		$doc = $this->createAtomDoc();
		$requestType = $this->getRequestType($requestItem);
		$data = $responseItem->getResponse();
		$userId = $requestItem->getUser()->getUserId($requestItem->getToken());
		$guid = 'urn:guid:' . $userId;
		$authorName = $_SERVER['HTTP_HOST'] . ':' . $userId;
		$updatedAtom = date(DATE_ATOM);
		
		// Check to see if this is a single entry, or a collection, and construct either an atom 
		// feed (collection) or an entry (single)		
		if ($responseItem->getResponse() instanceof RestFulCollection) {
			$totalResults = $responseItem->getResponse()->getTotalResults();
			$itemsPerPage = $requestItem->getCount();
			$startIndex = $requestItem->getStartIndex();
			
			// The root Feed element
			$entry = $this->addNode($doc, 'feed', '', false, self::$nameSpace);
			
			// Required Atom fields
			$endPos = ($startIndex + $itemsPerPage) > $totalResults ? $totalResults : ($startIndex + $itemsPerPage);
			$this->addNode($entry, 'title', $requestType . ' feed for id ' . $authorName . ' (' . $startIndex . ' - ' . ($endPos - 1) . ' of ' . $totalResults . ')');
			$author = $this->addNode($entry, 'author');
			$this->addNode($author, 'uri', $guid);
			$this->addNode($author, 'name', $authorName);
			$this->addNode($entry, 'updated', $updatedAtom);
			$this->addNode($entry, 'id', $guid);
			$this->addNode($entry, 'link', '', array('rel' => 'self', 
					'href' => 'http://' . $_SERVER['HTTP_HOST'] . $_SERVER['REQUEST_URI']));
			// Add osearch & next link to the entry
			$this->addPagingFields($entry, $startIndex, $itemsPerPage, $totalResults);
			// Add response entries to feed
			$responses = $responseItem->getResponse()->getEntry();
			foreach ($responses as $response) {
				// Attempt to have a real ID field, otherwise we fall back on the idSpec id
				$idField = is_object($response) && isset($response->id) ? $response->id : (is_array($response) && isset($response['id']) ? $response['id'] : $requestItem->getUser()->getUserId($requestItem->getToken()));
				// construct <entry> blocks this record
				$feedEntry = $this->addNode($entry, 'entry');
				$content = $this->addNode($feedEntry, 'content', '', array(
						'type' => 'application/xml'));
				// Author node
				$author = $this->addNode($feedEntry, 'author');
				$this->addNode($author, 'uri', $guid);
				$this->addNode($author, 'name', $authorName);
				// Special hoisting rules for activities
				if ($response instanceof Activity) {
					$this->addNode($feedEntry, 'category', '', array('term' => 'status'));
					$this->addNode($feedEntry, 'updated', date(DATE_ATOM, $response->postedTime));
					$this->addNode($feedEntry, 'id', 'urn:guid:' . $response->id);
					//FIXME should add a link field but don't have URL's available yet:
					// <link rel="self" type="application/atom+xml" href="http://api.example.org/activity/feeds/.../af3778"/>
					$this->addNode($feedEntry, 'title', strip_tags($response->title));
					$this->addNode($feedEntry, 'summary', $response->body);
					// Unset them so addData doesn't include them again
					unset($response->postedTime);
					unset($response->id);
					unset($response->title);
					unset($response->body);
				} else {
					$this->addNode($feedEntry, 'id', 'urn:guid:' . $idField);
					$this->addNode($feedEntry, 'title', $requestType . ' feed entry for id ' . $idField);
					$this->addNode($feedEntry, 'updated', $updatedAtom);
				}
				
				// recursively add responseItem data to the xml structure
				$this->addData($content, $requestType, $response, self::$osNameSpace);
			}
		} else {
			// Single entry = Atom:Entry	
			$entry = $doc->appendChild($doc->createElementNS(self::$nameSpace, "entry"));
			// Atom fields
			$this->addNode($entry, 'title', $requestType . ' entry for ' . $authorName);
			$author = $this->addNode($entry, 'author');
			$this->addNode($author, 'uri', $guid);
			$this->addNode($author, 'name', $authorName);
			$this->addNode($entry, 'id', $guid);
			$this->addNode($entry, 'updated', $updatedAtom);
			$content = $this->addNode($entry, 'content', '', array('type' => 'application/xml'));
			// addData loops through the responseItem data recursively creating a matching XML structure
			$this->addData($content, $requestType, $data, self::$osNameSpace);
		}
		$xml = $doc->saveXML();
		if ($responseItem->getResponse() instanceof RestFulCollection) {
			//FIXME dirty hack until i find a way to add multiple name spaces using DomXML functions
			$xml = str_replace('<feed xmlns="http://www.w3.org/2005/Atom">', '<feed xmlns="http://www.w3.org/2005/Atom" xmlns:osearch="http://a9.com/-/spec/opensearch/1.1">', $xml);
		}
		echo $xml;
	}

	function outputBatch(Array $responses, SecurityToken $token)
	{
		$this->boundryHeaders();
		foreach ($responses as $response) {
			$request = $response['request'];
			$response = $response['response'];
			// output buffering supports multiple levels of it.. it's a nice feature to abuse :)
			ob_start();
			$this->outputResponse($response, $request);
			$part = ob_get_contents();
			ob_end_clean();
			$this->outputPart($part, $response->getError());
		}
	}

	/**
	 * Easy shortcut for creating & appending XML nodes
	 *
	 * @param DOMElement $node node to append the new child node too
	 * @param string $name name of the new element
	 * @param string $value value of the element, if empty no text node is created
	 * @param array $attributes optional array of attributes, false by default. If set attributes are added to the node using the key => val pairs
	 * @param string $nameSpace optional namespace to use when creating node
	 * @return DOMElement node
	 */
	private function addNode($node, $name, $value = '', $attributes = false, $nameSpace = false)
	{
		if ($nameSpace) {
			$childNode = $node->appendChild($this->doc->createElementNS($nameSpace, $name));
		} else {
			$childNode = $node->appendChild($this->doc->createElement($name));
		}
		if (! empty($value) || $value == '0') {
			$childNode->appendChild($this->doc->createTextNode($value));
		}
		if ($attributes && is_array($attributes)) {
			foreach ($attributes as $attrName => $attrVal) {
				$childNodeAttr = $childNode->appendChild($this->doc->createAttribute($attrName));
				if (! empty($attrVal)) {
					$childNodeAttr->appendChild($this->doc->createTextNode($attrVal));
				}
			}
		}
		return $childNode;
	}

	/**
	 * Adds the osearch fields & generates a next link if result set > itemsPerPage
	 *
	 * @param DOMElement $entry the entry DOMElement to append the links too
	 * @param int $startIndex
	 * @param int $itemsPerPage
	 * @param int $totalResults
	 */
	private function addPagingFields($entry, $startIndex, $itemsPerPage, $totalResults)
	{
		$this->addNode($entry, 'osearch:totalResults', $totalResults);
		$this->addNode($entry, 'osearch:startIndex', $startIndex ? $startIndex : '0');
		$this->addNode($entry, 'osearch:itemsPerPage', $itemsPerPage);
		// Create a 'next' link based on our current url if this is a pageable collection & there is more to display
		if (($startIndex + $itemsPerPage) < $totalResults) {
			$nextStartIndex = ($startIndex + $itemsPerPage) - 1;
			if (($uri = $_SERVER['REQUEST_URI']) === false) {
				throw new Exception("Could not parse URI : {$_SERVER['REQUEST_URI']}");
			}
			$uri = parse_url($uri);
			$params = array();
			if (isset($uri['query'])) {
				parse_str($uri['query'], $params);
			}
			$params[RestRequestItem::$START_INDEX] = $nextStartIndex;
			$params[RestRequestItem::$COUNT] = $itemsPerPage;
			foreach ($params as $paramKey => $paramVal) {
				$outParams[] = $paramKey . '=' . $paramVal;
			}
			$outParams = '?' . implode('&', $outParams);
			$nextUri = 'http://' . $_SERVER['HTTP_HOST'] . $uri['path'] . $outParams;
			$this->addNode($entry, 'link', '', array('rel' => 'next', 'href' => $nextUri));
		}
	}

	/**
	 * Creates the root document using our xml version & charset
	 *
	 * @return DOMDocument
	 */
	private function createAtomDoc()
	{
		$this->doc = new DOMDocument(self::$xmlVersion, self::$charSet);
		$this->doc->formatOutput = self::$formatOutput;
		return $this->doc;
	}

	/**
	 * Extracts the Atom entity name from the request url
	 *
	 * @param RequestItem $requestItem the request item
	 * @return string the request type
	 */
	private function getRequestType($requestItem)
	{
		// map the Request URL to the content type to use  
		$params = $requestItem->getParameters();
		if (! is_array($params) || empty(self::$entryTypes[$params[0]])) {
			throw new Exception("Unsupported request type");
		}
		return self::$entryTypes[$params[0]];
	}

	/**
	 * Recursive function that maps an data array or object to it's xml represantation 
	 *
	 * @param DOMDocument $doc the root document
	 * @param DOMElement $element the element to append the new node(s) to
	 * @param string $name the name of the to be created node
	 * @param array or object $data the data to map to xml
	 * @param string $nameSpace if specified, the node is created using this namespace
	 * @return DOMElement returns newly created element
	 */
	private function addData(DOMElement $element, $name, $data, $nameSpace = false)
	{
		if ($nameSpace) {
			$newElement = $element->appendChild($this->doc->createElementNS($nameSpace, $name));
		} else {
			$newElement = $element->appendChild($this->doc->createElement($name));
		}
		if (is_array($data)) {
			foreach ($data as $key => $val) {
				if (is_array($val) || is_object($val)) {
					// prevent invalid names.. try to guess a good one :)
					if (is_numeric($key)) {
						$key = is_object($val) ? get_class($val) : $key = $name;
					}
					$this->addData($newElement, $key, $val);
				} else {
					if (is_numeric($key)) {
						$key = is_object($val) ? get_class($val) : $key = $name;
					}
					$elm = $newElement->appendChild($this->doc->createElement($key));
					$elm->appendChild($this->doc->createTextNode($val));
				}
			}
		} elseif (is_object($data)) {
			if ($data instanceof Enum) {
				// enums are output as : <NAME key="$key">$displayValue</NAME> 
				$keyEntry = $newElement->appendChild($this->doc->createAttribute('key'));
				$keyEntry->appendChild($this->doc->createTextNode($data->key));
				$newElement->appendChild($this->doc->createTextNode($data->getDisplayValue()));
			
			} else {
				$vars = get_object_vars($data);
				foreach ($vars as $key => $val) {
					if (is_array($val) || is_object($val)) {
						// prevent invalid names.. try to guess a good one :)
						if (is_numeric($key)) {
							$key = is_object($val) ? get_class($val) : $key = $name;
						}
						$this->addData($newElement, $key, $val);
					} else {
						$elm = $newElement->appendChild($this->doc->createElement($key));
						$elm->appendChild($this->doc->createTextNode($val));
					}
				}
			}
		} else {
			$newElement->appendChild($this->doc->createTextNode($data));
		}
		return $newElement;
	}
}
