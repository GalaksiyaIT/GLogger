package com.galaksiya.logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ObjectMessage;

import java.util.Map;

/**
 * Log4j2 implementation of the {@link LoggerAdapter}. This class is the default logging method to choose when nothing
 * is specified with the configuration file.
 *
 * @author Berkay Akdal
 * @version 1.0.0 02.04.2019
 * @since 2.0.0
 */
class Log4j2LoggerAdapterImpl implements LoggerAdapter {

	/**
	 * Log4j2 {@link Logger} instance to wrap and use for printing system logs.
	 */
	private Logger logger;

	/**
	 * Constructs a new {@link GLogger} instance  with wrapping the given log4j2 {@link Logger} instance. Each class
	 * must provide their own logger instances.
	 *
	 * @param type Class to create and wrap a Log4j2 {@link Logger} instance for.
	 */
	Log4j2LoggerAdapterImpl(final Class<?> type) {
		this.logger = LogManager.getLogger(type);
	}

	/**
	 * Logs given message after formatting it with given array of <code>params</code> using the {@link
	 * String#format(String, Object...)} method, on debug level, if and only if the debug level is enabled. <br> Uses
	 * {@link Logger#isDebugEnabled()} method to check if the debug logs are enabled.
	 *
	 * @param message Log message to format and print.
	 * @param params  Additional parameters to format the given <code>message</code> with.
	 */
	@Override
	public void debug(String message, Object... params) {
		debug(message, null, params);
	}

	@Override
	public void debug(Map<String, Object> map) {
		Level level = this.logger
				.getLevel();
		if (this.logger.isDebugEnabled()) {
			this.logger.debug(new ObjectMessage(map));
		}
	}

	/**
	 * Works exactly like {@link #debug(String, Object...)}, additionally appends the given {@link Throwable} to the log
	 * message.
	 *
	 * @param message   Log message to format and print.
	 * @param throwable Occurred exception to append to the log.
	 * @param params    Additional parameters to format the given <code>message</code> with.
	 */
	@Override
	public void debug(String message, Throwable throwable, Object... params) {
		if (this.logger.isDebugEnabled()) {
			if (params == null || params.length == 0) {
				this.logger.debug(message, throwable);
			} else {
				this.logger.debug(String.format(message, params), throwable);
			}
		}
	}

	/**
	 * Logs given message after formatting it with given array of <code>params</code> using the {@link
	 * String#format(String, Object...)} method, on error level.
	 *
	 * @param message Log message to format and print.
	 * @param params  Additional parameters to format the given <code>message</code> with.
	 */
	@Override
	public void error(String message, Object... params) {
		error(message, null, params);
	}

	/**
	 * Works exactly like {@link #error(String, Object...)}, additionally appends the given {@link Throwable} to the log
	 * message.
	 *
	 * @param message   Log message to format and print.
	 * @param throwable Occurred exception to append to the log.
	 * @param params    Additional parameters to format the given <code>message</code> with.
	 */
	@Override
	public void error(String message, Throwable throwable, Object... params) {
		if (this.logger.isErrorEnabled()) {
			if (params == null || params.length == 0) {
				this.logger.error(message, throwable);
			} else {
				this.logger.error(String.format(message, params), throwable);
			}
		}
	}

	@Override
	public void error(Map<String, Object> map) {
		if (this.logger.isErrorEnabled()) {
			this.logger.error(new ObjectMessage(map));
		}
	}

	@Override
	public void error(Map<String, Object> map, Throwable t) {
		if (this.logger.isErrorEnabled()) {
			this.logger.error(new ObjectMessage(map), t);
		}
	}

	/**
	 * Logs given message after formatting it with given array of <code>params</code> using the {@link
	 * String#format(String, Object...)} method, on fatal level.
	 *
	 * @param message Log message to format and print.
	 * @param params  Additional parameters to format the given <code>message</code> with.
	 */
	@Override
	public void fatal(String message, Object... params) {
		fatal(message, null, params);
	}

	/**
	 * Works exactly like {@link #fatal(String, Object...)}, additionally appends the given {@link Throwable} to the log
	 * message.
	 *
	 * @param message   Log message to format and print.
	 * @param throwable Occurred exception to append to the log.
	 * @param params    Additional parameters to format the given <code>message</code> with.
	 */
	@Override
	public void fatal(String message, Throwable throwable, Object... params) {
		if (this.logger.isFatalEnabled()) {
			this.logger.fatal(String.format(message, params), throwable);
		}
	}

	@Override
	public void fatal(Map<String, Object> map) {
		if (this.logger.isFatalEnabled()) {
			this.logger.fatal(new ObjectMessage(map));
		}
	}

	/**
	 * Logs given message after formatting it with given array of <code>params</code> using the {@link
	 * String#format(String, Object...)} method, on info level, if and only if the info level is enabled. <br> Uses
	 * {@link Logger#isInfoEnabled()} method to check if the trace logs are enabled.
	 *
	 * @param message Log message to format and print.
	 * @param params  Additional parameters to format the given <code>message</code> with.
	 */
	@Override
	public void info(String message, Object... params) {
		info(message, null, params);
	}

	/**
	 * Works exactly like {@link #info(String, Object...)}, additionally appends the given {@link Throwable} to the log
	 * message.
	 *
	 * @param message   Log message to format and print.
	 * @param throwable Occurred exception to append to the log.
	 * @param params    Additional parameters to format the given <code>message</code> with.
	 */
	@Override
	public void info(String message, Throwable throwable, Object... params) {
		if (this.logger.isInfoEnabled()) {
			if (params == null || params.length == 0) {
				this.logger.info(message, throwable);
			} else {
				this.logger.info(String.format(message, params), throwable);
			}
		}
	}

	@Override
	public void info(Map<String, Object> map) {
		if (this.logger.isInfoEnabled()) {
			this.logger.info(new ObjectMessage(map));
		}
	}

	/**
	 * Logs given message after formatting it with given array of <code>params</code> using the {@link
	 * String#format(String, Object...)} method, on trace level, if and only if the trace level is enabled. <br> Uses
	 * {@link Logger#isTraceEnabled()} method to check if the trace logs are enabled.
	 *
	 * @param message Log message to format and print.
	 * @param params  Additional parameters to format the given <code>message</code> with.
	 */
	@Override
	public void trace(String message, Object... params) {
		trace(message, null, params);
	}

	/**
	 * Works exactly like {@link #trace(String, Object...)}, additionally appends the given {@link Throwable} to the log
	 * message.
	 *
	 * @param message   Log message to format and print.
	 * @param throwable Occurred exception to append to the log.
	 * @param params    Additional parameters to format the given <code>message</code> with.
	 */
	@Override
	public void trace(String message, Throwable throwable, Object... params) {
		if (this.logger.isTraceEnabled()) {
			if (params == null || params.length == 0) {
				this.logger.trace(message, throwable);
			} else {
				this.logger.trace(String.format(message, params), throwable);
			}
		}
	}

	@Override
	public void trace(Map<String, Object> map) {
		if (this.logger.isTraceEnabled()) {
			this.logger.trace(new ObjectMessage(map));
		}
	}

	/**
	 * Logs given message after formatting it with given array of <code>params</code> using the {@link
	 * String#format(String, Object...)} method, on warn level.
	 *
	 * @param message Log message to format and print.
	 * @param params  Additional parameters to format the given <code>message</code> with.
	 */
	@Override
	public void warn(String message, Object... params) {
		warn(message, null, params);
	}

	/**
	 * Works exactly like {@link #warn(String, Object...)}, additionally appends the given {@link Throwable} to the log
	 * message.
	 *
	 * @param message   Log message to format and print.
	 * @param throwable Occurred exception to append to the log.
	 * @param params    Additional parameters to format the given <code>message</code> with.
	 */
	@Override
	public void warn(String message, Throwable throwable, Object... params) {
		if (params == null || params.length == 0) {
			this.logger.warn(message, throwable);
		} else {
			this.logger.warn(String.format(message, params), throwable);
		}
	}

	@Override
	public void warn(Map<String, Object> map) {
		if (this.logger.isWarnEnabled()) {
			this.logger.warn(new ObjectMessage(map));
		}
	}

	@Override
	public void warn(Map<String, Object> map, Throwable t) {
		if (this.logger.isWarnEnabled()) {
			this.logger.warn(new ObjectMessage(map), t);
		}
	}

	@Override
	public Level getLevel() {
		return this.logger.getLevel();
	}

}
