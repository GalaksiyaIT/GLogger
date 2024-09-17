package com.galaksiya.logger.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

/**
 * Configuration loader class {@link com.galaksiya.logger.GLogger} module. The properties this class loads are as
 * follows;
 * <pre>
 * com.galaksiya.logger {
 *     useGcpLogging = boolean
 *     gcpLogging {
 *         logName = String
 *         severityLevel = String (one of [TRACE, DEBUG, INFO, WARN, ERROR, FATAL])
 *         credentials = String (path of the authentication json file)
 *     }
 * }
 * </pre>
 *
 * @author Berkay Akdal
 * @author Uğur Üntürk
 * @version 1.2.0, 10.05.2019
 * @since 2.0.0
 */
public class GLoggerConfig {

	private static final String COM_GALAKSIYA_LOGGING = "com.galaksiya.logging";
	private static final String LOG_CONTEXT_FIELD_NAME = "logContextFieldName";
	private static final String USE_GCP_LOGGING = "useGcpLogging";
	private static final String GCP_LOGGING_SEVERITY_LEVEL = "gcpLogging.severityLevel";
	private static final String GCP_LOGGING_CREDENTIALS = "gcpLogging.credentials";

	/**
	 * Singleton configuration class instance.
	 */
	private static GLoggerConfig instance;

	/**
	 * Getter for the singleton {@link GLoggerConfig} instance.
	 */
	public static GLoggerConfig getInstance() {
		if (instance == null) {
			synchronized (GLoggerConfig.class) {
				if (instance == null) {
					instance = new GLoggerConfig();
				}
			}
		}
		return instance;
	}

	/**
	 * {@link Config} instance for reading configuration file.
	 */
	private Config config;

	/**
	 * Default constructor made private to prevent outside access. Use {@link #getInstance()} instead to get the
	 * singleton instance.
	 */
	private GLoggerConfig() {
		config = ConfigFactory.load();
	}

	public String getProjectId() {
		return getGLoggerConfig().getString("gcpLogging.projectId");
	}

	/**
	 * Getter for the module sub config in the configuration file.
	 *
	 * @return com.galaksiya.logging sub-config.
	 */
	private Config getGLoggerConfig() {
		return this.config.getConfig(COM_GALAKSIYA_LOGGING);
	}

	/**
	 * Getter for the GCP logging usage indicator configuration. If not found, returns false as default value.
	 *
	 * @return The value of com.galaksiya.logging.useGcpLogging specified in configuration file.
	 */
	public boolean isGcpLoggingEnabled() {
		boolean isEnabled = false;
		try {
			isEnabled = getGLoggerConfig().getBoolean(USE_GCP_LOGGING);
		} catch (ConfigException ignored) {
		}
		return isEnabled;
	}

	/**
	 * Getter for the GCP log severity level configuration.
	 *
	 * @return The value of com.galaksiya.logging.gcpLogging.severityLevel specified in configuration file.
	 */
	public String getGcpSeverityLevel() {
		return getGLoggerConfig().getString(GCP_LOGGING_SEVERITY_LEVEL);
	}

	/**
	 * Getter for the GCP log credentials path configuration.
	 *
	 * @return The value of <i>com.galaksiya.logging.gcpLogging.credentials</i> configuration.
	 */
	public String getGcpLoggingCredentialsPath() {
		return getGLoggerConfig().getString(GCP_LOGGING_CREDENTIALS);
	}

	/**
	 * Getter for the log context field name, if not present, {@code _contextId} will be used.
	 *
	 * @return The value of <i>com.galaksiya.logging.logContextFieldName</i> configuration.
	 */
	public String getLogContextName() {
		String config;
		try {
			config = getGLoggerConfig().getString(LOG_CONTEXT_FIELD_NAME);
		} catch (Throwable t) {
			config = "_contextId";
		}
		return config;
	}
}
