package com.galaksiya.logger;

import com.galaksiya.logger.config.GLoggerConfig;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.MonitoredResource;
import com.google.cloud.logging.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Google Cloud Platform logger implementation of the {@link LoggerAdapter}. It will send log messages to the
 * Stackdriver from the current Google App Engine of Google Compute Engine it's working on. To use this logger,
 * com.galaksiya.logger.useGcpLogging property must be configured as true, and com.galaksiya.logger.gcpLogging.logName
 * and com.galaksiya.logger.gcpLogging.severityLevel properties must be specified.
 *
 * @author Berkay Akdal
 * @author Uğur Üntürk
 * @version 1.1.0, 10.05.2019
 * @since 2.0.0
 */
class GcpLoggerAdapterImpl implements LoggerAdapter {

	/**
	 * Constant to bind simple message logs. Message logs will be send in a JSON with <i>_message</i> property.
	 */
	private static final String MESSAGE = "_message";

	/**
	 * Constant to bind stack traces. Stack traces will be send a JSON with <i>_stackTrace</i> property.
	 */
	private static final String STACK_TRACE = "_stackTrace";

	private Logging logging;
	private String logName;
	private int severityLevel;

	GcpLoggerAdapterImpl(Class<?> type) {
		GLoggerConfig gLoggerConfig = GLoggerConfig.getInstance();
		severityLevel = Arrays.asList("TRACE", "DEBUG", "INFO", "", "WARN", "ERROR", "", "", "FATAL")
				.indexOf(gLoggerConfig.getGcpSeverityLevel());
		logName = type.getName();
		try {
			FileInputStream credentialsStream = new FileInputStream(gLoggerConfig.getGcpLoggingCredentialsPath());
			logging = LoggingOptions.newBuilder().setCredentials(ServiceAccountCredentials.fromStream(
					credentialsStream)).setProjectId(gLoggerConfig.getProjectId()).build().getService();
		} catch (Exception e) {
			logging = LoggingOptions.getDefaultInstance().getService();
			warn("an error occurred during initializing gcp logging with service credentials", e);
		}
	}

	@Override
	public void debug(String message, Object... params) {
		if (severityLevel < 2) {
			Map<String, Object> map = new HashMap<>();
			map.put(MESSAGE, String.format(message, params));

			sendLogAsynchronously(Payload.JsonPayload.of(map), Severity.DEBUG);
		}
	}

	@Override
	public void debug(Map<String, Object> map) {
		if (severityLevel < 2) {
			sendLogAsynchronously(Payload.JsonPayload.of(map), Severity.DEBUG);
		}
	}

	@Override
	public void debug(String message, Throwable throwable, Object... params) {
		if (severityLevel < 2) {
			Map<String, Object> map = new HashMap<>();
			map.put(MESSAGE, String.format(message, params));
			map.put(STACK_TRACE, ExceptionUtils.getStackTrace(throwable));

			sendLogAsynchronously(Payload.JsonPayload.of(map), Severity.DEBUG);
		}
	}

	@Override
	public void error(String message, Object... params) {
		if (severityLevel < 6) {
			Map<String, Object> map = new HashMap<>();
			map.put(MESSAGE, String.format(message, params));

			sendLogAsynchronously(Payload.JsonPayload.of(map), Severity.ALERT);
		}
	}

	@Override
	public void error(String message, Throwable throwable, Object... params) {
		if (severityLevel < 6) {
			Map<String, Object> map = new HashMap<>();
			map.put(MESSAGE, String.format(message, params));
			map.put(STACK_TRACE, ExceptionUtils.getStackTrace(throwable));

			sendLogAsynchronously(Payload.JsonPayload.of(map), Severity.ALERT);
		}
	}

	@Override
	public void error(Map<String, Object> map) {
		if (severityLevel < 6) {
			sendLogAsynchronously(Payload.JsonPayload.of(map), Severity.ALERT);
		}
	}

	@Override
	public void error(Map<String, Object> map, Throwable t) {
		if (severityLevel < 6) {
			map.put(STACK_TRACE, ExceptionUtils.getStackTrace(t));

			sendLogAsynchronously(Payload.JsonPayload.of(map), Severity.ALERT);
		}
	}

	@Override
	public void fatal(String message, Object... params) {
		if (severityLevel < 9) {
			Map<String, Object> map = new HashMap<>();
			map.put(MESSAGE, String.format(message, params));

			sendLogAsynchronously(Payload.JsonPayload.of(map), Severity.EMERGENCY);
		}
	}

	@Override
	public void fatal(String message, Throwable throwable, Object... params) {
		if (severityLevel < 9) {
			Map<String, Object> map = new HashMap<>();
			map.put(MESSAGE, String.format(message, params));
			map.put(STACK_TRACE, ExceptionUtils.getStackTrace(throwable));

			sendLogAsynchronously(Payload.JsonPayload.of(map), Severity.EMERGENCY);
		}
	}

	@Override
	public void fatal(Map<String, Object> map) {
		if (severityLevel < 9) {
			sendLogAsynchronously(Payload.JsonPayload.of(map), Severity.EMERGENCY);
		}
	}

	@Override
	public void info(String message, Object... params) {
		if (severityLevel < 3) {
			Map<String, Object> map = new HashMap<>();
			map.put(MESSAGE, String.format(message, params));

			sendLogAsynchronously(Payload.JsonPayload.of(map), Severity.INFO);
		}
	}

	@Override
	public void info(String message, Throwable throwable, Object... params) {
		if (severityLevel < 3) {
			Map<String, Object> map = new HashMap<>();
			map.put(MESSAGE, String.format(message, params));
			map.put(STACK_TRACE, ExceptionUtils.getStackTrace(throwable));

			sendLogAsynchronously(Payload.JsonPayload.of(map), Severity.INFO);
		}
	}

	@Override
	public void info(Map<String, Object> map) {
		if (severityLevel < 3) {
			sendLogAsynchronously(Payload.JsonPayload.of(map), Severity.INFO);
		}
	}

	@Override
	public void trace(String message, Object... params) {
		if (severityLevel == 0) {
			Map<String, Object> map = new HashMap<>();
			map.put(MESSAGE, String.format(message, params));

			sendLogAsynchronously(Payload.JsonPayload.of(map), Severity.DEFAULT);
		}
	}

	@Override
	public void trace(String message, Throwable throwable, Object... params) {
		if (severityLevel == 0) {
			Map<String, Object> map = new HashMap<>();
			map.put(MESSAGE, String.format(message, params));
			map.put(STACK_TRACE, ExceptionUtils.getStackTrace(throwable));

			sendLogAsynchronously(Payload.JsonPayload.of(map), Severity.DEFAULT);
		}
	}

	@Override
	public void trace(Map<String, Object> map) {
		if (severityLevel == 0) {
			sendLogAsynchronously(Payload.JsonPayload.of(map), Severity.DEFAULT);
		}
	}

	@Override
	public void warn(String message, Object... params) {
		if (severityLevel < 5) {
			Map<String, Object> map = new HashMap<>();
			map.put(MESSAGE, String.format(message, params));

			sendLogAsynchronously(Payload.JsonPayload.of(map), Severity.WARNING);
		}
	}

	@Override
	public void warn(String message, Throwable throwable, Object... params) {
		if (severityLevel < 5) {
			Map<String, Object> map = new HashMap<>();
			map.put(MESSAGE, String.format(message, params));
			map.put(STACK_TRACE, ExceptionUtils.getStackTrace(throwable));

			sendLogAsynchronously(Payload.JsonPayload.of(map), Severity.WARNING);
		}
	}

	@Override
	public void warn(Map<String, Object> map) {
		if (severityLevel < 5) {
			sendLogAsynchronously(Payload.JsonPayload.of(map), Severity.WARNING);
		}
	}

	@Override
	public void warn(Map<String, Object> map, Throwable t) {
		if (severityLevel < 5) {
			map.put(STACK_TRACE, ExceptionUtils.getStackTrace(t));

			sendLogAsynchronously(Payload.JsonPayload.of(map), Severity.WARNING);
		}
	}

	@Override
	public Level getLevel() {
		return Level.INFO;
	}

	private void sendLogAsynchronously(Payload payload, Severity severity) {
		LogEntry log = LogEntry.newBuilder(payload).setSeverity(severity).setLogName(this.logName)
				.setResource(MonitoredResource.newBuilder("global").build()).build();
		logging.write(Collections.singleton(log));
		logging.flush();
	}
}
