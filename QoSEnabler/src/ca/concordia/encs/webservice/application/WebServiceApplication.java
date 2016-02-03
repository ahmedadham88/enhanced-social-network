package ca.concordia.encs.webservice.application;

import org.apache.log4j.Logger;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

import ca.concordia.encs.diameter.base.avp.SessionID;
import ca.concordia.encs.services.DiameterManager;
import ca.concordia.encs.services.NotificationManger;
import ca.concordia.encs.services.QoSManager;
import ca.concordia.encs.webservice.resources.*;

import java.io.*;


public class WebServiceApplication extends Application {

	private static final Logger LOGGER = Logger
			.getLogger(WebServiceApplication.class);

	private static DiameterManager diameterManager = null;
	private static NotificationManger notificationManger = null;
	private static QoSManager qosManager = null;

	public static void main(String[] args) throws Exception {

		LOGGER.info("Starting QoSEnabler web service ...");

		File diameterConfigFile = new File(
				ApplicationConfiguration.getXmlConfigFilename());
		if (!diameterConfigFile.exists()) {
			LOGGER.error("The diameter configuration file does not exist.");
			return;
		}

		// Create the HTTP server and listen on a port
		Server server = new Server(Protocol.HTTP,
				ApplicationConfiguration.getWebServicAddress(),
				ApplicationConfiguration.getWebServicPort());
		// Server server = new Server(Protocol.HTTP, port);

		// WebServiceApplication a = new WebServiceApplication();
		// a.setInboundRoot(a.createInboundRoot());
		server.setNext(new WebServiceApplication());
		// TODO:Check why it causes a problem
		Context context = new Context();
		context.getParameters().add("lowThreads", "15");
		context.getParameters().add("maxThreads", "20480");//512
		context.getParameters().add("maxTotalConnections", "40000");
		server.setContext(context);
		// server.getContext().getParameters().add("maxThreads", "512");
		// Creating router and attach resources

		try {

			qosManager = new QoSManager();
			notificationManger = new NotificationManger();
			diameterManager = new DiameterManager(
					ApplicationConfiguration.getXmlConfigFilename(),
					ApplicationConfiguration.getDestinationHost(),
					ApplicationConfiguration.getDestinationRealm());

			server.start();
			LOGGER.info("QoSEnabler web service listens to "
					+ ApplicationConfiguration.getWebServicAddress() + ":"
					+ ApplicationConfiguration.getWebServicPort());

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Failed to start QoSEnabler web service.");
		}

	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach(ApplicationConfiguration.getWebServiceResourcesAddress()
				+ "/sessions", SessionsResource.class);
		router.attach(ApplicationConfiguration.getWebServiceResourcesAddress()
				+ "/sessions/{sessionId}", SessionResource.class);

		// router.attach(ApplicationConfiguration.getWebServiceResourcesAddress()
		// + "/notification_subscriptiob",
		// NotificationSubscriptionResource.class);
		// router.attach(ApplicationConfiguration.getWebServiceResourcesAddress()
		// + "/sessions/{sessionId}", NotificationSubscriptionsResource.class);
		return router;

	}

	private static void Stop() {

		// LOGGER.info("Stop method called.");
		// if (componenet != null) {
		// try {
		// componenet.stop();
		// diameterManager.shutdown();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// LOGGER.info("Failed to stop QoSEnabler web service.");
		// }
		// }

	}

}

// public class WebServiceApplication extends Application {
//
// private static final Logger LOGGER = Logger
// .getLogger(WebServiceApplication.class);
//
// private static Component componenet = null;
// private static DiameterManager diameterManager = null;
// private static NotificationManger notificationManger = null;
//
// public static void main(String[] args) throws Exception {
//
// LOGGER.info("Starting QoSEnabler web service ...");
//
// File diameterConfigFile = new File(
// ApplicationConfiguration.getXmlConfigFilename());
// if (!diameterConfigFile.exists()) {
// LOGGER.error("The diameter configuration file does not exist.");
// return;
// }
//
// componenet = new Component();
// // Create the HTTP server and listen on a port
// Server server = new Server(Protocol.HTTP,
// ApplicationConfiguration.getWebServicAddress(),
// ApplicationConfiguration.getWebServicPort());
// // Server server = new Server(Protocol.HTTP, port);
// server.setNext(new WebServiceApplication());
// componenet.getServers().add(server);
//
// server.getContext().getParameters().add("maxThreads", "512");
// // Creating router and attach resources
// Router router = new Router(componenet.getContext().createChildContext());
// router.attach("/sessions", SessionsResource.class);
// router.attach("/sessions/{sessionId}", SessionResource.class);
// componenet.getDefaultHost().attach(router);
//
// // VirtualHost v = new VirtualHost(componenet.getContext());
// // v.setHostDomain("www.qos.test");
// // v.setHostPort(Integer.toString(ApplicationConfiguration
// // .getWebServicPort()));
// // v.attach(router);
// // componenet.getDefaultHost().attach(router);
//
// // componenet.getHosts().add(v);
// // componenet.updateHosts();
// // componenet.getDefaultHost().attach("/restlet", router);
//
// try {
//
// notificationManger = new NotificationManger();
// diameterManager = new DiameterManager(
// ApplicationConfiguration.getXmlConfigFilename(),
// ApplicationConfiguration.getDestinationHost(),
// ApplicationConfiguration.getDestinationRealm(),
// notificationManger);
// componenet.start();
// LOGGER.info("QoSEnabler web service listens to "
// + ApplicationConfiguration.getWebServicAddress() + ":"
// + ApplicationConfiguration.getWebServicPort());
//
// } catch (Exception e) {
// e.printStackTrace();
// LOGGER.error("Failed to start QoSEnabler web service.");
// }
//
// }
//
// private static void Stop() {
//
// LOGGER.info("Stop method called.");
// if (componenet != null) {
// try {
// componenet.stop();
// diameterManager.shutdown();
// } catch (Exception e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// LOGGER.info("Failed to stop QoSEnabler web service.");
// }
// }
//
// }
//
// }
