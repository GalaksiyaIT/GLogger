package com.galaksiya.logger;

import com.galaksiya.logger.config.GLoggerConfig;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static org.apache.logging.log4j.Level.INFO;

/**
 * The main purpose of this class is to wrap log4j2 {@link Logger} and simplify logging by automating necessary controls
 * the developers must do each time before printing a system log. It has methods for each level of logging and the
 * methods basically just checks if the level of logging is enabled at the time when the log is getting printed. With
 * the usage of this class developers don't have to do this check on each logging:
 * <pre>
 * if (logger.isTraceEnabled()) {
 * 	logger.trace(String.format("Some log message", someParams));
 * }
 * </pre>
 * Instead they can use the related method with the log level:
 * <pre>
 * this.trace("Some log message", someParams);
 * </pre>
 * To use this logger, classes must provide their own {@link Logger}s while instantiating it.
 *
 * @author Berkay Akdal
 * @version 1.0.0, 16.03.2018
 * @since 1.0.0
 */
public class GLogger {

	/**
	 * Logger adapter to use on this GLogger.
	 */
	private LoggerAdapter log;

	/**
	 * Constructs a new {@link GLogger} instance. <br> {@link GLogger} uses {@link Log4j2LoggerAdapterImpl} by default.
	 * It can be changed by configuring com.galaksiya.logger.useGcpLogger property as true.
	 *
	 * @param type Class to create and wrap a Log4j2 {@link Log4j2LoggerAdapterImpl} instance or use as the logger name
	 *             for {@link GcpLoggerAdapterImpl}.
	 */
	public GLogger(final Class<?> type) {
		this.log = GLoggerConfig.getInstance().isGcpLoggingEnabled() ?
				new GcpLoggerAdapterImpl(type) : new Log4j2LoggerAdapterImpl(type);
	}

	/**
	 * Logs given message after formatting it with given array of <code>params</code> using the {@link
	 * String#format(String, Object...)} method, on debug level, if and only if the debug level is enabled. <br> Uses
	 * {@link Logger#isDebugEnabled()} method to check if the debug logs are enabled.
	 *
	 * @param message Log message to format and print.
	 * @param params  Additional parameters to format the given <code>message</code> with.
	 */
	public void debug(String message, Object... params) {
		log.debug(message, params);
	}

	public void debug(Map<String, Object> map) {
		log.debug(map);
	}

	/**
	 * Works exactly like {@link #debug(String, Object...)}, additionally appends the given {@link Throwable} to the log
	 * message.
	 *
	 * @param message   Log message to format and print.
	 * @param throwable Occurred exception to append to the log.
	 * @param params    Additional parameters to format the given <code>message</code> with.
	 */
	public void debug(String message, Throwable throwable, Object... params) {
		log.debug(message, throwable, params);
	}

	/**
	 * Logs given message after formatting it with given array of <code>params</code> using the {@link
	 * String#format(String, Object...)} method, on error level.
	 *
	 * @param message Log message to format and print.
	 * @param params  Additional parameters to format the given <code>message</code> with.
	 */
	public void error(String message, Object... params) {
		log.error(message, params);
	}

	/**
	 * Works exactly like {@link #error(String, Object...)}, additionally appends the given {@link Throwable} to the log
	 * message.
	 *
	 * @param message   Log message to format and print.
	 * @param throwable Occurred exception to append to the log.
	 * @param params    Additional parameters to format the given <code>message</code> with.
	 */
	public void error(String message, Throwable throwable, Object... params) {
		log.error(message, throwable, params);
	}

	public void error(Map<String, Object> map) {
		log.error(map);
	}

	public void error(Map<String, Object> map, Throwable t) {
		log.error(map, t);
	}

	public void fatal(Map<String, Object> map) {
		log.fatal(map);
	}

	/**
	 * Logs given message after formatting it with given array of <code>params</code> using the {@link
	 * String#format(String, Object...)} method, on fatal level.
	 *
	 * @param message Log message to format and print.
	 * @param params  Additional parameters to format the given <code>message</code> with.
	 */
	public void fatal(String message, Object... params) {
		log.fatal(message, params);
	}

	/**
	 * Works exactly like {@link #fatal(String, Object...)}, additionally appends the given {@link Throwable} to the log
	 * message.
	 *
	 * @param message   Log message to format and print.
	 * @param throwable Occurred exception to append to the log.
	 * @param params    Additional parameters to format the given <code>message</code> with.
	 */
	public void fatal(String message, Throwable throwable, Object... params) {
		log.fatal(message, throwable, params);
	}

	/**
	 * Logs given message after formatting it with given array of <code>params</code> using the {@link
	 * String#format(String, Object...)} method, on info level, if and only if the info level is enabled. <br> Uses
	 * {@link Logger#isInfoEnabled()} method to check if the trace logs are enabled.
	 *
	 * @param message Log message to format and print.
	 * @param params  Additional parameters to format the given <code>message</code> with.
	 */
	public void info(String message, Object... params) {
		log.info(message, params);
	}

	/**
	 * Works exactly like {@link #info(String, Object...)}, additionally appends the given {@link Throwable} to the log
	 * message.
	 *
	 * @param message   Log message to format and print.
	 * @param throwable Occurred exception to append to the log.
	 * @param params    Additional parameters to format the given <code>message</code> with.
	 */
	public void info(String message, Throwable throwable, Object... params) {
		log.info(message, throwable, params);
	}

	public void info(Map<String, Object> map) {
		log.info(map);
	}

	public void trace(Map<String, Object> map) {
		log.trace(map);
	}

	/**
	 * Logs given message after formatting it with given array of <code>params</code> using the {@link
	 * String#format(String, Object...)} method, on trace level, if and only if the trace level is enabled. <br> Uses
	 * {@link Logger#isTraceEnabled()} method to check if the trace logs are enabled.
	 *
	 * @param message Log message to format and print.
	 * @param params  Additional parameters to format the given <code>message</code> with.
	 */
	public void trace(String message, Object... params) {
		log.trace(message, params);
	}

	/**
	 * Works exactly like {@link #trace(String, Object...)}, additionally appends the given {@link Throwable} to the log
	 * message.
	 *
	 * @param message   Log message to format and print.
	 * @param throwable Occurred exception to append to the log.
	 * @param params    Additional parameters to format the given <code>message</code> with.
	 */
	public void trace(String message, Throwable throwable, Object... params) {
		log.trace(message, throwable, params);
	}

	/**
	 * Logs given message after formatting it with given array of <code>params</code> using the {@link
	 * String#format(String, Object...)} method, on warn level.
	 *
	 * @param message Log message to format and print.
	 * @param params  Additional parameters to format the given <code>message</code> with.
	 */
	public void warn(String message, Object... params) {
		log.warn(message, params);
	}

	/**
	 * Works exactly like {@link #warn(String, Object...)}, additionally appends the given {@link Throwable} to the log
	 * message.
	 *
	 * @param message   Log message to format and print.
	 * @param throwable Occurred exception to append to the log.
	 * @param params    Additional parameters to format the given <code>message</code> with.
	 */
	public void warn(String message, Throwable throwable, Object... params) {
		log.warn(message, throwable, params);
	}

	public void warn(Map<String, Object> map) {
		log.warn(map);
	}

	public void warn(Map<String, Object> map, Throwable t) {
		log.warn(map, t);
	}

	/**
	 * Starts a new operation log by creating an {@link OperationLog} with given name.
	 *
	 * @param name Name of the current operation to create the {@link OperationLog} with.
	 * @return Created {@link OperationLog}.
	 */
	public OperationLog startOperation(String name) {
		return new OperationLog(name, this, INFO).logStart();
	}

	/**
	 * Starts a new operation log by creating an {@link OperationLog} with given name.
	 *
	 * @param name         Name of the current operation to create the {@link OperationLog} with.
	 * @param exitLogLevel Log level to use while printing the exit log of the operation.
	 * @return Created {@link OperationLog}.
	 */
	public OperationLog startOperation(String name, Level exitLogLevel) {
		return new OperationLog(name, this, exitLogLevel).logStart();
	}

	/**
	 * Starts a new operation log by creating an {@link OperationLog} with given name.
	 *
	 * @param name  Name of the current operation to create the {@link OperationLog} with.
	 * @param logId Context id of a previous OperationLog to link with the new one.
	 * @return Created {@link OperationLog}.
	 */
	public OperationLog startOperation(String name, String logId) {
		return new OperationLog(name, this, INFO).chain(logId).logStart();
	}

	/**
	 * Starts a new operation log by creating an {@link OperationLog} with given name.
	 *
	 * @param name         Name of the current operation to create the {@link OperationLog} with.
	 * @param logId        Context id of a previous OperationLog to link with the new one.
	 * @param exitLogLevel Log level to use while printing the exit log of the operation.
	 * @return Created {@link OperationLog}.
	 */
	public OperationLog startOperation(String name, String logId, Level exitLogLevel) {
		return new OperationLog(name, this, exitLogLevel).chain(logId).logStart();
	}

	/**
	 * Getter for the current log level configuration.
	 *
	 * @return Returns the current log level.
	 */
	Level getLevel() {
		return log.getLevel();
	}
}
