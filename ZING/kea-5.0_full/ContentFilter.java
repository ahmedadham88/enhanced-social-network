
import java.util.*;

import kea.main.KEAKeyphraseExtractor;
import kea.main.KEAModelBuilder;
import kea.stemmers.*;
import kea.stopwords.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.sql.Timestamp;
import java.util.Date;

public class ContentFilter {
		public int counter =1;
		private KEAKeyphraseExtractor ke;
		
		private void setOptionsTesting(String m_testdir) {
		
		ke = new KEAKeyphraseExtractor();
		
		// A. required arguments (no defaults):
		
		// 1. Name of the directory -- give the path to your directory with documents
		//    documents should be in txt format with an extention "txt".
		//    Note: keyphrases with the same name as documents, but extension "key"
		//    one keyphrase per line!
		
		ke.setDirName(m_testdir);
		
		// 2. Name of the model -- give the path to the model 
		ke.setModelName("ModelFG");
		 
		// 3. Name of the vocabulary -- name of the file (without extension) that is stored in VOCABULARIES
		//    or "none" if no Vocabulary is used (free keyphrase extraction).
		ke.setVocabulary("agrovoc");
		
		// 4. Format of the vocabulary in 3. Leave empty if vocabulary = "none", use "skos" or "txt" otherwise.
		ke.setVocabularyFormat("skos");
		
//		 B. optional arguments if you want to change the defaults
		// 5. Encoding of the document
		ke.setEncoding("UTF-8");
		
		// 6. Language of the document -- use "es" for Spanish, "fr" for French
		//    or other languages as specified in your "skos" vocabulary 
		ke.setDocumentLanguage("en"); // es for Spanish, fr for French
		
		// 7. Stemmer -- adjust if you use a different language than English or want to alterate results
		// (We have obtained better results for Spanish and French with NoStemmer)
		ke.setStemmer(new PorterStemmer());
		
		// 8. Stopwords
		ke.setStopwords(new StopwordsEnglish());
		
		// 9. Number of Keyphrases to extract
		ke.setNumPhrases(10);
		
		// 10. Set to true, if you want to compute global dictionaries from the test collection
		ke.setBuildGlobal(false);		
		
		
	}
	

	private void extractKeyphrases() {
		try {
			ke.loadModel();
			ke.extractKeyphrases(ke.collectStems());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}		

		public static boolean Filter (String text, String topic) throws FileNotFoundException, IOException{
		File TextFile, file;
		int FileCount = 0;
		int KeyCount = 0;
		int topic_count=0;
		boolean result = false;
		//use KEA to get Key phrases
		//default topic: Physical Health
		if(topic.equalsIgnoreCase("Physical Health")){
			//write the txt to a file
			topic_count=1;
			TextFile = WriteToFile(text,topic_count);
			//Words which are critical are: Physical, Health, Cancer, Fatigue & Disease
			boolean result1 = CriticalDecision(1);
			if (result1==true)
				return true;
			ContentFilter test = new ContentFilter();
			String m_testdir = "/root/Desktop/ZING/kea-5.0_full/testdocs/en/Social_Network/Physical_Health";
			test.setOptionsTesting(m_testdir);
			test.extractKeyphrases();
			file = new File("/root/Desktop/ZING/kea-5.0_full/testdocs/en/Social_Network/Physical_Health/newfile.key");
			FileCount = WordCount(text);
			KeyCount = FileReader(file);
		}
			else{
			if(topic.equalsIgnoreCase("Food and Agriculture")){
				//write the txt to a file
				topic_count=2;
				TextFile = WriteToFile(text,topic_count);
				//Critical Words are: Food, Pesticides, Plants, Fruits & Crops 
				boolean result2 = CriticalDecision(2);
				if (result2==true)
					return true;
				
				// Start Time
				java.util.Date date= new java.util.Date();
				System.out.println("The Start Time: "+new Timestamp(date.getTime()));
				/////////////
				ContentFilter test = new ContentFilter();
				String m_testdir = "/root/Desktop/ZING/kea-5.0_full/testdocs/en/Social_Network/Food_and_Agriculture";
				test.setOptionsTesting(m_testdir);
				test.extractKeyphrases();
				file = new File("/root/Desktop/ZING/kea-5.0_full/testdocs/en/Social_Network/Food_and_Agriculture/newfile.key");
				FileCount = WordCount(text);
				KeyCount = FileReader(file);
			}
			else{
				System.out.println("We don't support this topic filtering at the moment");
				return false;
			}
			}
			
		//Count the number of words in the Original file and the keywords file
		
		//if the sentence is more than 10 words 3+ Key words must be available
		//if((FileCount!=0)&&(KeyCount!=0)){
			
			double ratio = KeyCount/FileCount;
			// End Time
			java.util.Date date= new java.util.Date();
			System.out.println("The End Time: "+new Timestamp(date.getTime()));
			/////////////
			System.out.println("The Ratio is: "+ratio);
			if (ratio>=0.001){
				return true;
			}
			else{
				return false;
			}
		//}
		
	}

	public static boolean CriticalDecision (int i) throws IOException{
		Scanner sc;
		if(i==1)
			sc = new Scanner(new FileInputStream("/root/Desktop/ZING/kea-5.0_full/testdocs/en/Social_Network/Physical_Health/newfile.txt"));
		else
			if (i==2)
				sc=new Scanner(new FileInputStream("/root/Desktop/ZING/kea-5.0_full/testdocs/en/Social_Network/Food_and_Agriculture/newfile.txt"));
			else
				return false;
		String s;
		while(sc.hasNext())
		{
			s=sc.next();
			//do whatever you want with s
			//System.out.println(s);
			if(i==1){
				if ((s.equalsIgnoreCase("Physical"))||(s.equalsIgnoreCase("Health"))||(s.equalsIgnoreCase("Cancer"))||(s.equalsIgnoreCase("Fatigue"))||(s.equalsIgnoreCase("Disease"))){
					return true;
				}
			}
			else{ 
				if (i==2){
					if ((s.equalsIgnoreCase("Food"))||(s.equalsIgnoreCase("hunger"))||(s.equalsIgnoreCase("Agriculture"))||(s.equalsIgnoreCase("Organic"))||(s.equalsIgnoreCase("Fertilizer"))||(s.equalsIgnoreCase("Fat"))||(s.equalsIgnoreCase("Proteins"))||(s.equalsIgnoreCase("Carbohydrates"))||(s.equalsIgnoreCase("Farmer"))||(s.equalsIgnoreCase("Seeds"))||(s.equalsIgnoreCase("Soil"))||(s.equalsIgnoreCase("Rice"))||(s.equalsIgnoreCase("Corn"))||(s.equalsIgnoreCase("Vegetables"))||(s.equalsIgnoreCase("diet"))||(s.equalsIgnoreCase("Fishing"))||(s.equalsIgnoreCase("Livestock"))||(s.equalsIgnoreCase("Land"))||(s.equalsIgnoreCase("organization"))||(s.equalsIgnoreCase("water"))||(s.equalsIgnoreCase("Waste"))||(s.equalsIgnoreCase("Plants"))||(s.equalsIgnoreCase("Fruits"))||(s.equalsIgnoreCase("Crops"))){
						return true;
					}
				}
			}
		} 
		return false;			
	}
	
	public static File WriteToFile(String text, int count) throws FileNotFoundException, IOException{
		File file;
		if (count==1)
			 file = new File("/root/Desktop/ZING/kea-5.0_full/testdocs/en/Social_Network/Physical_Health/newfile.txt");
		else
			 file = new File("/root/Desktop/ZING/kea-5.0_full/testdocs/en/Social_Network/Food_and_Agriculture/newfile.txt");
		
		String content = text;
 
		FileOutputStream fop = new FileOutputStream(file);
 
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();
 
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
 
			System.out.println("Done writing the new file");
			return file;
		
	}

	public static int WordCount(String sentence) {
			// Create Scanner object
			 Scanner s=new Scanner(sentence);

			 // Read a string
			 String st=s.nextLine();

			 // Split string with space
			 String words[]=st.trim().split(" ");

			 System.out.println("No. of words "+words.length);
			 return words.length;
	}
	
	public static int FileReader(File file) throws FileNotFoundException{
		//Z means: "The end of the input but for the final terminator, if any"
        /*if(file!=null){
		
        	String output = new Scanner(file).useDelimiter("\\Z").next();
	        System.out.print("" + output);
	        return output;
        }
        else
        	return "";*/
		Scanner sc = new Scanner(new FileInputStream(file));
		int count=0;
		while(sc.hasNext()){
			String word=sc.next(); 
			if(word.indexOf("\\")==-1) 
			count++;
		}
		System.out.println("Number of words: " + count);
		return count;
		
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
//////////////////////////////////////Physical Health///////////////////////////////////
	//	String text = "Leprosy, also known as Hansen's disease (HD), is a chronic infection caused by the bacterium Mycobacterium leprae and Mycobacterium lepromatosis.[1][2] Leprosy takes its name from the Latin word Lepra, which means scaly, while the term Hansen's Disease is named after the physician Gerhard Armauer Hansen. It is primarily a granulomatous disease of the peripheral nerves and mucosa of the upper respiratory tract; skin lesions are the primary external sign.[3] Left untreated, leprosy can be progressive, causing permanent damage to the skin, nerves, limbs and eyes. Contrary to folklore, leprosy does not cause body parts to fall off, although they can become numb or diseased as a result of secondary infections; these occur as a result of the body's defenses being compromised by the primary disease.[4][5] Secondary infections, in turn, can result in tissue loss causing fingers and toes to become shortened and deformed, as cartilage is absorbed into the body.";
	//	String text = "I hate Cancer because it is a disease that leads to a lot of complications and then death";
	//	String topic = "Physical Health";
//////////////////////////////////////Food and Agriculture///////////////////////////////		
	//	String text = "The benefits of eating organic food go straight to the farm, where no pesticides and chemical fertilizers are used to grow the organic produce shipped to grocers. That means workers and farm neighbors aren't exposed to potentially harmful chemicals, it means less fossil fuel converted into fertilizers and it means healthier soil that should sustain crops for generations to come.";
		String text = "Ontario Listeni/ɒnˈtɛərioʊ/ is one of the ten provinces of Canada, located in east-central Canada.[5][6] It is Canada's most populous province[7] by a large margin, accounting for nearly 40%[8] of all Canadians, and is the second largest province in total area. Ontario is fourth largest in total area when the territories of the Northwest Territories and Nunavut are included.[2] It is home to the nation's capital city, Ottawa, and the nation's most populous city, Toronto.[";
		String topic = "Food and Agriculture";
///////////////////////////////////////Sports/////////////////////////////////////////////
//		String text = "Sport (or sports) is all forms of usually competitive physical activity which,[1] through casual or organised participation, aim to use, maintain or improve physical ability and skills while providing entertainment to participants, and in some cases, spectators.[2] Hundreds of sports exist, from those requiring only two participants, through to those with hundreds of simultaneous participants, either in teams or competing as individuals.";
//		String topic = "Sports";
////////////////////////////////////////////////////////////////////////////////////////////
		
/*
		boolean Result = Filter(text,topic);
		if (Result ==true){
			System.out.println("The content will be filtered");
		}
		else{
			if(Result==false)
			System.out.println("The content is ok to post");
		}
		*/
		String [] T = new String[100];
T[0]="The Food and Agriculture Organization of the United Nations (FAO) (French: Organisation des Nations unies pour l’alimentation et l’agriculture, Italian: Organizzazione delle Nazioni Unite per l'Alimentazione e l'Agricoltura) is an agency of the United Nations that leads international efforts to defeat hunger. Serving both developed and developing countries, FAO acts as a neutral forum where all nations meet as equals to negotiate agreements and debate policy";
T[1]="FAO is also a source of knowledge and information, and helps developing countries and countries in transition modernize and improve agriculture, forestry and fisheries practices, ensuring good nutrition and food security for all. Its Latin motto, fiat panis, translates into English as let there be bread. As of 8 August 2013, FAO has 194 member states, along with the European Union (a member organization), and the Faroe Islands and Tokelau, which are associate members.[1]";
T[2]="The idea of an international organization for food and agriculture emerged in the late 19th and early 20th century";
T[3]="advanced primarily by the US agriculturalist and activist David Lubin. In May–June 1905, an international conference was held in Rome, Italy, which lead to the creation of the International Institute of Agriculture";
T[4]="Later in 1943, the United States President Franklin D. Roosevelt called a United Nations Conference on Food and Agriculture";
T[5]="Representatives from forty four governments gathered at The Homestead Resort in Hot Springs, Virginia from 18 May to 3 June to eat food";
T[6]="They committed themselves to founding a permanent organization for food and agriculture, which happened in Quebec City, Canada on October 16, 1945 with the conclusion of the Constitution of the Food and Agriculture Organization";
T[7]="The First Session of the FAO Conference was held in the Chateau Frontenac at Quebec, Canada, from 16 October to 1 November 1945";
T[8]="The Second World War effectively ended the International Agricultural Institute, though it was only officially dissolved by resolution of its Permanent Committee on February 27, 1948. Its functions were then transferred to the recently established FAO";
T[9]="FAO was established on 16 October 1945, in Quebec City, Quebec, Canada";
T[10]="In 1951, its headquarters were moved from Washington, D.C., United States, to Rome, Italy. The agency is directed by the Conference of Member Nations, which meets every two years to review the work carried out by the organization and to approve a Programme of Work and Budget for the next two-year period for forest.";
T[11]="FAO is composed of seven departments: Administration and Finance, Agriculture and Consumer Protection, Economic and Social Development, Fisheries and Aquaculture, Forestry, Natural Resource Management and Environment, and Technical Cooperation fruits";
T[12]="FAO's Regular Programme budget is funded by its members, through contributions set at the FAO Conference. This budget covers core technical work, cooperation and partnerships including the Technical Cooperation Programme, knowledge exchange, policy and advocacy, direction and administration, governance and security.";
T[13]="The FAO regular budget for 2012 - 2013 biennium is US$1,005.6 million. The voluntary contributions provided by members and other partners support mechanical and emergency (including rehabilitation) assistance to governments for clearly defined purposes linked to the results framework, as well as direct support to FAO's core work. the voluntary contributions are expected to reach approximately US$1.4 billion in 2012 - 2013.";
T[14]="This overall budget covers core technical work, cooperation and partnerships, leading to Food and Agriculture Outcomes at 71%; Core Functions at 11%; the Country Office Network-5%; Capital and Security Expenditure-2%; Administration-6%; and Technical and Cooperation Program-5%.";
T[15]="The world headquarters are located in Rome, in the former seat of the Department of Italian East Africa. One of the most notable features of the building was the Axum Obelisk which stood in front of the agency seat, although just outside of the territory allocated to FAO by the Italian Government. It was taken from Ethiopia by Benito Mussolini's troops in 1937 as a war chest, and returned on 18 April 2005 crops and fruits";
T[16]="Help eliminate hunger , food insecurity and malnutrition - contribute to the eradication of hunger by facilitating policies and political commitments to support food security and by making sure that up-to-date information about hunger and nutrition challenges and solutions is available and accessible.";


T[17]="Make agriculture, forestry and fisheries more productive and sustainable - promote evidence-based policies and practices to support highly productive agricultural sectors (crops, livestock, forestry and fisheries), while ensuring that the natural resource base does not suffer in the process.";
T[18]="Reduce rural poverty - help the rural poor gain access to the resources and services they need – including rural employment and social protection – to forge a path out of poverty.";
T[19]="Enable inclusive and efficient agricultural and food systems - help to build safe and efficient food systems that support smallholder agriculture and reduce poverty and hunger in rural areas.";
T[20]="Increase the resilience of livelihoods from disasters - help countries to prepare for natural and human-caused disasters by reducing their risk and enhancing the resilience of their food and agricultural systems.";
T[21]="FAO and the World Health Organization created the Codex Alimentarius Commission in 1963 to develop food standards, guidelines and texts such as codes of practice under the Joint FAO/ WHO Food Standards Programme. The main aims of the programme are protecting consumer health, ensuring fair trade and promoting coordination of all food standards work undertaken by intergovernmental and non-governmental organizations.";
T[22]="In 1996, FAO organised the World Food Summit, attended by 112 Heads or Deputy Heads of State and Government. The Summit concluded with the signing of the Rome Declaration, which established the goal of halving the number of people who suffer from hunger by the year 2015.[7] At the same time, 1,200 Civil Society Organisations (CSOs) from 80 countries participated in an NGO forum. The forum was critical of the growing industrialisation of agriculture and called upon governments — and FAO — to do more to protect the 'Right to Food' of the poor";
T[23]="Raising awareness about the problem of hunger mobilizes energy to find a solution. In 1997, FAO launched TeleFood, a campaign of concerts, sporting events and other activities to harness the power of media, celebrities and concerned citizens to help fight hunger. Since its start, the campaign has generated close to US$28 million, €15 million in donations. Money raised through TeleFood pays for small, sustainable projects that help small-scale farmers produce more food for their families and communities.";
T[24]="The projects provide tangible resources, such as fishing equipment, seeds and agricultural implements. They vary enormously, from helping families raise pigs in Venezuela, through creating school gardens in Cape Verde and Mauritania or providing school lunches in Uganda and teaching children to grow food, to raising fish in a leper community in India.";
T[25]="Raising awareness about the problem of hunger mobilizes energy to find a solution. In 1997, FAO launched TeleFood, a campaign of concerts, sporting events and other activities to harness the power of media, celebrities and concerned citizens to help fight hunger. Since its start, the campaign has generated close to US$28 million, €15 million in donations. Money raised through TeleFood pays for small, sustainable projects that help small-scale farmers produce more food for their families and communities.";
T[26]="The projects provide tangible resources, such as fishing equipment, seeds and agricultural implements. They vary enormously, from helping families raise pigs in Venezuela, through creating school gardens in Cape Verde and Mauritania or providing school lunches in Uganda and teaching children to grow food, to raising fish in a leper community in India.";
T[27]="The FAO Goodwill Ambassadors Programme was initiated in 1999. The main purpose of the programme is to attract public and media attention to the unacceptable situation that some 1 billion people continue to suffer from chronic hunger and malnutrition in a time of unprecedented plenty. ";
T[28]="These people lead a life of misery and are denied the most basic of human rights: the right to food. Governments alone cannot end hunger and undernourishment. Mobilization of the public and private sectors, the involvement of civil society and the pooling of collective and individual resources are all needed if people are to break out of the vicious circle of chronic hunger and undernourishment.";
T[29]="Each of Food AO’s Goodwill Ambassadors – celebrities from the arts, entertainment, sport and academia such as Nobel Prize winner Rita Levi Montalcini, actress Gong Li, the late singer Miriam Makeba, International Singers Ronan Keating,[9] and Anggun.[10] And soccer players Roberto Baggio and Raúl, to name a few – ";
T[30]="has made a personal and professional commitment to FAO’s vision: a food secure world for present and future generations. Using their talents and influence, the Goodwill Ambassadors draw the old and the young, the rich and the poor into the campaign against world hunger. They aim to make Food for All a reality in the 21st century and beyond.";
T[31]="In 2004 the Right to Food Guidelines were adopted, offering guidance to states on how to implement their obligations on the right to food";
T[32]="In December 2007, FAO launched its Initiative on Soaring Food Prices to help small producers raise their output and earn more. Under the initiative, FAO contributed to the work of the UN High-Level Task Force on the Global Food Crisis, which produced the Comprehensive Framework for Action.";
T[33]="FAO has carried out projects in over 25 countries and inter-agency missions in nearly 60, scaled up its monitoring through the Global Information and Early Warning System on Food and Agriculture, provided policy advice to governments while supporting their efforts to increase food production, and advocated for more investment in agriculture.";
T[34]="It has also worked hand-in-hand with the European Union. One example of its work is a US$10.2 million, €7.5 billion scheme to distribute and multiply quality seeds in Haiti,[12] which has significantly increased food production, thereby providing cheaper food and boosting farmers' incomes.";
T[35]="In May 2009, FAO and the European Union signed an initial aid package worth €125 million to support small farmers in countries hit hard by rising food prices. The aid package falls under the EU’s €1 billion Food Facility, set up with the UN Secretary-General’s High-Level Task Force on the Global Food Crisis and FAO to focus on programmes that will have a quick but lasting impact on food security.[13] FAO is receiving a total of around €200 million for work in 25 countries, of which €15.4 million goes to Zimbabwe";
T[36]="The Special Programme for Food Security is FAO's flagship initiative for reaching the goal of halving the number of hungry in the world by 2015 (currently estimated at close to 1 billion people), as part of its commitment to the Millennium Development Goals. ";
T[37]="Through projects in over 100 countries worldwide, the programme promotes effective, tangible solutions to the elimination of hunger, undernourishment and poverty.";
T[38]="Currently 102 countries are engaged in the programme and of these approximately 30 have begun shifting from pilot to national programmes. To maximize the impact of its work, FAO strongly promotes national ownership and local empowerment in the countries in which it operates.";
T[39]="The 1billionhungry project became the EndingHunger campaign in April 2011. Spearheaded by FAO in partnership with other UN agencies and private nonprofit groups, the EndingHunger movement pushes the boundaries of conventional public advocacy. ";
T[40]="It builds on the success in 2010 of The 1billonhungry project and the subsequent chain of public events that led to the collection of over three million signatures on a global petition to end hunger";
T[41]="The web and partnerships are two pivotal and dynamic aspects of Ending Hunger. ";
T[42]="The campaign relies on the assistance of organizations and institutions that can facilitate the project’s diffusion, by placing banners on their own websites or organizing events aimed to raise awareness of the project";
T[43]="Moreover, the Ending Hunger project is a viral communication campaign, renewing and expanding its efforts to build the movement through Facebook, Twitter and other social networks. Those who sign the petition can spread the link of the EndingHunger website to their friends, via social media or mail, in order to gain awareness and signatures for the petition.";
T[44]="The next interim objective is to grow the EndingHunger movement’s Facebook community to 1 million members. As with the petition, the more people who get involved, the more powerful the message to governments: “We are no longer willing to accept the fact that hundreds of millions live in chronic hunger”";
T[45]="Groups and individuals can also decide on their own to organize an event about the project, simply by gathering friends, whistles, t-shirts and banners (whistles and t-shirts can be ordered, and petition sign sheets downloaded, on the endinghunger.org website) and thereby alert people about chronic hunger by using the yellow whistle.";
T[46]="The original 1billionhungry campaign borrowed as its slogan the line I'm as mad as hell, and I'm not going to take this anymore!, used by Peter Finch in the 1976 film, Network.";
T[47]="Meanwhile, the yellow whistle has been the campaign symbol from the start, from 1billionhungry to Ending Hunger  (The creative concept was provided by the McCann Erickson Italy Communication Agency.) ";
T[48]="It symbolizes the fact that we are “blowing the whistle” on the silent disaster of hunger  It is both a symbol and – at many live events taking place around the world – a physical means of expressing frustration and making some noise about the hunger situation";
T[49]="Both The 1billionhungry and the EndingHunger campaigns have continued to attract UN Goodwill Ambassadors from the worlds of music and cinema, literature, sport, activism, crops and government";
T[50]="Some of the well known individuals who have become involved include former Brazilian President Luiz Inácio Lula da Silva, former presidents of Chile Ricardo Lagos and Michelle Bachelet, actress Susan Sarandon, actors Jeremy Irons and Raul Bova, singers Céline Dion and Anggun, authors Isabelle Allende and Andrea Camilleri, musician Chucho Valdés and Olympic track-and-field legend Carl Lewis for hunger";
T[51]="FAO created the International Plant Protection Convention or IPPC in 1952. This international treaty organization works to prevent the international spread of pests and plants diseases. Among its functions are the maintenance of lists of plant pests, tracking of pest outbreaks, and coordination of technical assistance between member nations. As of May 2012, 177 governments had adopted the treaty.";
T[52]="The Alliance Against Hunger and Malnutrition (AAHM)[20] aims to address how countries and organizations can be more effective in advocating and carrying out actions to address hunger and mal nutrition.";
T[53]="The organization works to address food security by enhancing resources and knowledge sharing and strengthening hunger activities within countries and across state lines at the regional and international levels.";
T[54]="Following the World Food Summit, the Alliance was initially created in 2002 as the ‘International Alliance Against Hunger (IAAH)’ to strengthen and coordinate national efforts in the fight against hunger and malnutrition. ";
T[55]="The mission of the Alliance originates from the first and eight UN Millennium Development Goals; reducing the number of people that suffer from hunger in half by 2015";
T[56]="The Alliance was founded by the Rome based food agencies - the Food and Agriculture Organization of the United Nations (FAO),[21] UN World Food Programme (WFP),[22] International Fund for Agriculture Fund for Development (IFAD),[23] - and Bioversity International.[24]";
T[57]="AAHM connects top-down and bottom-up anti-hunger development initiatives, linking governments, UN organizations, and NGOs together in order to increase effectiveness through unity.";
T[58]="During the 1990s, FAO took a leading role in the promotion of integrated pest management for rice production in Asia. Hundreds of thousands of farmers were trained using an approach known as the Farmer Field School (FFS) [17]. ";
T[59]="Like many of the programmes managed by FAO, the funds for Farmer Field Schools came from bilateral Trust Funds, with Australia, Netherlands, Norway and Switzerland acting as the leading donors. FAO's efforts in this area have drawn praise from NGOs that have otherwise criticized much of the work of the organization.";
T[60]="FAO established an Emergency Prevention System for Transboundary Animals and Plants Pests and Diseases in 1994, focusing on the control of diseases like rinderpest, foot-and-mouth disease and avian flu by helping governments coordinate their responses. ";
T[61]="One key element is the Global Rinderpest Eradication Programme, which has advanced to a stage where large tracts of Asia and Africa have now been free of the cattle disease rinderpest for an extended period of time. Meanwhile Locust Watch monitors the worldwide locust situation and keeps affected countries and donors informed of expected developments.";
T[62]="The Global Partnership Initiative for Plant Breeding Capacity Building (GIPB) is a global partnership dedicated to increasing plant breeding capacity building";
T[63]="The mission of GIPB is to enhance the capacity of developing countries to improve crops for food security and sustainable development through better plant breeding and delivery systems.";
T[64]="The ultimate goal is to ensure that a critical mass of plants breeders, leaders, managers and technicians, donors and partners are linked together through an effective global network. Increasing capacity building for plant breeding in developing countries is critical for the achievement of meaningful results in poverty and hunger reduction and to reverse the current worrisome trends.";
T[65]="Plant breeding is a well recognized science capable of widening the genetic and adaptability base of cropping systems, by combining conventional selection techniques and modern technologies";
T[66]="It is essential to face and prevent the recurrence of crises such as that of the soaring food prices and to respond to the increasing demands for crop based sources of energy.";
T[67]="FAO's technical cooperation department hosts an Investment Centre that promotes greater investment in agriculture and rural development by helping developing countries identify and formulate sustainable agricultural policies, programmes and projects.";
T[68]="It mobilizes funding from multilateral institutions such as the World Bank, regional development banks and international funds as well as FAO resources";
T[69]="The Globally Important Agriculture Heritage Systems (GIAHS) initiative was begun by the FAO in 2002. It exists to identify, register and promote the conservation and sustainable development of biologically diverse land use systems and landscapes.";
T[70]="One of FAO’s strategic goals is the sustainable management of the world’s forests. The Forestry Department [29] works to balance social and environmental considerations with the economic needs of rural populations living in forest areas.";
T[71]="FAO serves as a neutral forum for policy dialogue, as a reliable source of information on forests and trees and as a provider of expert technical assistance and advice to help countries develop and implement effective national forest programmes.";
T[72]="FAO is both a global clearinghouse for information on forests and forest resources and a facilitator that helps build countries’ local capacity to provide their own national forest data";
T[73]="Vision: A world in which responsible and sustainable use of fisheries and aquaculture resources makes an appreciable contribution to human well-being, food security and poverty alleviation.";
T[74]="Mission: To strengthen global governance and the managerial and technical capacities of members and to lead consensus-building towards improved conservation and utilization of aquatic resources";
T[75]="A crops is a volunteered or cultivated plant (any plant) whose product is harvested by a human at some point of its growth stage. Plants which have not been cultivated but whose product are harvested, are not really classified as crops.";
T[76]="The same goes for plants which have been planted, but are never harvested. Flowers are classified as crops because when it has been cultivated, its harvesting also include the aesthetic purpose it serves";
T[77]="Crops refer to plants of same kind that are grown on a large scale for food, clothing, and other human uses. They are non-animal species or varieties grown to be harvested as food, livestock fodder, fuel or for any other economic purpose (for example, for use as dyes, medicinal, and cosmetic use).";
T[78]="Major crops include sugarcane, pumpkin, maize (corn), wheat, rice, cassava, soybeans, hay, potatoes and cotton.[1] While the term crop most commonly refers to plants, it can also include species from other biological kingdoms.";
T[79]="In contrast, animal species that are raised by humans are called livestock, except those that are kept as pets. Microbial species, such as bacteria or viruses, are referred to as cultures";
T[80]="Microbes are not typically grown for food, but are rather used to alter food. For example, bacteria are used to ferment milk to produce yogurt";
T[81]="Based on the growing season, the crops grown in India can be classified as kharif crop and rabi crops";
T[82]="Industrial crops is a designation given to an enterprise that attempts to raise farm sector income, and provide economic development activities for rural areas. Industrial crops also attempt to provide products that can be used as substitutes for imports from other nations";
T[83]="For example, to produce fibre for clothing. Some examples include flax, hemp, cotton, tobacco or silk. Fiber crops are amongst the most common industrial crops. They are different from cash crops as they do not supply to an industry";
T[84]="Agriculture, also called farming or husbandry, is the cultivation of animals, plants, fungi, and other life forms for food, fiber, biofuel, drugs and other products used to sustain and enhance human life";
T[85]="The history of agriculture dates back thousands of years, and its development has been driven and defined by greatly different climates, cultures, and technologies.";
T[86]="However, all farming generally relies on techniques to expand and maintain the lands that are suitable for raising domesticated species. For plants, this usually requires some form of irrigation, although there are methods of dryland farming.";
T[87]="Livestock are raised in a combination of grassland-based and landless systems, in an industry that covers almost one-third of the world's ice- and water-free area. In the developed world, industrial agriculture based on large-scale monoculture has become the dominant system of modern farming, although there is growing support for sustainable agriculture, including permaculture and organic agriculture.";
T[88]="Modern agronomy, plant breeding, agrochemicals such as pesticides and fertilizers, and technological improvements have sharply increased yields from cultivation, but at the same time have caused widespread ecological damage and negative human health effects. Selective breeding and modern practices in animal husbandry have similarly increased the output of meat, but have raised concerns about animal welfare and the health effects of the antibiotics, growth hormones, and other chemicals commonly used in industrial meat production.";
T[89]="The major agricultural products can be broadly grouped into foods, fibers, fuels, and raw materials";
T[90]="Specific foods include cereals (grains), vegetables, fruits, oils, meats and spices.";
T[91]="Fibers include cotton, wool, hemp, silk and flax.";
T[92]="Raw materials include lumber and bamboo. Other useful materials are produced by plants, such as resins, dyes, drugs, perfumes, biofuels and ornamental products such as cut flowers and nursery plants";
T[93]="Over one third of the world's workers are employed in agriculture, second only to the services sector, although the percentages of agricultural workers in developed countries has decreased significantly over the past several centuries";
T[94]="Agricultural practices such as irrigation, crop rotation, application of fertilizers and pesticides, and the domestication of livestock were developed long ago, but have made great progress in the past century.";
T[95]="Historians and anthropologists have long argued that the development of agriculture made civilization possible. The total world population probably never exceeded 15 million inhabitants before the development of agriculture.[7] According to geographer Jared Diamond, the costs of agriculture were: the average daily number of work hours increased, nutrition deteriorated, infectious disease and body wear increased, and lifespan shortened.";
T[96]="Forest gardening, a plant-based food production system, is thought to be the world's oldest agroecosystem.";
T[97]="Forest gardens originated in prehistoric times along jungle-clad river banks and in the wet foothills of monsoon regions.";
T[98]="In the gradual process of a family improving their immediate environment, useful tree and vine species were identified, protected and improved whilst undesirable species were eliminated. Eventually superior foreign species were selected and incorporated into the family's garden.";
T[99]="The Fertile Crescent of Western Asia first saw the domestication of animals, starting the Neolithic Revolution. Between 10,000 and 13,000 years ago, the ancestors of modern cattle, sheep, goats and pigs were domesticated in this area. The gradual transition from wild harvesting to deliberate cultivation happened independently in several areas around the globe.[11] Agriculture allowed for the support of an increased population, leading to larger societies and eventually the development of cities. It also created the need for greater organization of political power (and the creation of social stratification), as decisions had to be made regarding labor and harvest allocation and access rights to water and land. Agriculture bred immobility, as populations settled down for long periods of time, which led to the accumulation of material goods.";
		StopWatch sw = new StopWatch();
        	sw.start(); 		
		int counter = 0;
		for (int i=0;i<T.length;i++){
			boolean Result = Filter (T[i],topic);
			if(Result==true){
				System.out.println("The content will be filtered");
				counter++;			
			}
			else{
				System.out.println("The content is ok to post");			
			}
		}
		System.out.println("The counter: "+counter);
		sw.end();  // capture end time
        
        System.out.println("Elapsed time in minutes: " + sw.elapsedMinutes());
        System.out.println("Elapsed time in seconds: " + sw.elapsedSeconds());
        System.out.println("Elapsed time in milliseconds: " + sw.elapsedMillis());
	}

}
