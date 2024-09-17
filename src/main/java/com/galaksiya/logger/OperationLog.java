package com.galaksiya.logger;

import com.galaksiya.logger.config.GLoggerConfig;
import org.apache.logging.log4j.Level;

import java.util.AbstractMap.SimpleEntry;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import static org.apache.logging.log4j.Level.*;

/**
 * Logs events in JSON format. Operation logs are intended to use on method specific level, means that
 * <ol>
 * <li>An operation log must be created at the first line of a method,</li>
 * <li>Get its parameters while the method continues to run,</li>
 * <li>Log the result of the method, and the parameters the object has gathered</li>
 * </ol>
 * An operation log can be initialized calling the startOperation method on GLogger class and can be terminated via one
 * of its termination methods;
 * <ul>
 * <li>{@link #succeed()}</li>
 * <li>{@link #warn()}</li>
 * <li>{@link #fail()}</li>
 * </ul>
 * The logger will create a log with TRACE level initially and by default an INFO log when gets completed.
 * <br>
 * Exit log level can be overwritten while creating the operation log. Accepted levels are, TRACE, DEBUG and INFO.
 * <br>
 * An example usage of operation logs is as follows:
 * <pre>
 * private GLogger logger = new GLogger(Bar.class);
 *
 * public void foo(bar) {
 *     OperationLog operation = logger.startOperation("sampleOperation");
 *     // ...
 *     try {
 *     	   // Add fields to current operation.
 *         operation.addField("someField", "someValue").addField("someOtherField", 5);
 *         // ...
 *         operation.succeed(); // Log fields with INFO level and total elapsed time.
 *     } catch(Exception e) {
 *         operation.warn(e); // Log fields and given exception with WARN level.
 *     } catch(Throwable t) {
 *         operation.fail(t); // Log fields and given exception with ERROR level.
 *     }
 * }
 * </pre>
 *
 * @author Berkay Akdal
 * @author Uğur Üntürk
 * @version 1.2.0 16.07.2020
 * @since 1.0.2
 */
public class OperationLog {

	private static final String OPERATION_NAME = "_operationName";
	private static final String OPERATION_STATUS = "_operationStatus";
	private static final String OPERATION_TOOK = "_operationTook";
	private static final String OPERATION_STARTED = "_operationStarted";

	/**
	 * Creation time namely operation start time for the related operation.
	 */
	private long operationStartTime;

	/**
	 * Logger to use to print the logs. An external logger is required to use instead of creating a self logger for the
	 * {@link OperationLog} class to distinct the operation logs class by class.
	 */
	private GLogger logger;

	/**
	 * Log level to use while printing the {@link #succeed()} log.
	 */
	private final Level exitLogLevel;

	/**
	 * Holds the fields and values related with this operation.
	 */
	private Map<String, Entry<Level, Object>> fields;

	/**
	 * Indicates whether this operation log is completed or not.
	 */
	private boolean flushed;

	/**
	 * Default constructor made package private to prevent the uncontrolled creation of the operation logs.
	 */
	OperationLog() {
		this.exitLogLevel = INFO;
	}

	/**
	 * Creates a new operation log.
	 *
	 * @param operationName Name of the operation that is being performed.
	 * @param logger        Logger instance to use to print the logs.
	 * @param exitLogLevel  Level to use while printing the exit log.
	 */
	OperationLog(String operationName, GLogger logger, Level exitLogLevel) {
		this.logger = logger;
		this.exitLogLevel = exitLogLevel;

		this.fields = new HashMap<>();
		this.fields.put(OPERATION_NAME, new SimpleEntry<>(INFO, operationName));
		this.fields.put(GLoggerConfig.getInstance().getLogContextName(),
				new SimpleEntry<>(INFO, UUID.randomUUID().toString()));
		this.fields.put(OPERATION_STATUS, new SimpleEntry<>(INFO, "started"));
	}

	/**
	 * Overwrites the contextId of this OperationLog with the specified value to be able to track the linked
	 * operations.
	 *
	 * @param contextId Identifier of a previous operation is expected.
	 * @return This {@link OperationLog} object to chaining methods.
	 */
	OperationLog chain(String contextId) {
		if (contextId != null) {
			this.fields.put(GLoggerConfig.getInstance().getLogContextName(), new SimpleEntry<>(INFO, contextId));
		}
		return this;
	}

	/**
	 * Adds the given field to this operation log's fields.
	 * <br>
	 * Added fields will be put on a {@link HashMap}, so adding multiple fields with the same name will overwrite the
	 * previous ones.
	 * <br>
	 * Uses level as {@link Level#INFO}.
	 *
	 * @param name  Name of the field to add.
	 * @param value Value of the field.
	 * @return This {@link OperationLog} object to chaining methods.
	 */
	public OperationLog addField(String name, Object value) {
		return addField(name, value, INFO);
	}

	/**
	 * Adds the given field to this operation log's fields.
	 * <br>
	 * Added fields will be put on a {@link HashMap}, so adding multiple fields with the same name will overwrite the
	 * previous ones.
	 *
	 * @param name  Name of the field to add.
	 * @param value Value of the field.
	 * @param level Level of logging which this parameter must be printed.
	 * @return This {@link OperationLog} object to chaining methods.
	 */
	public OperationLog addField(String name, Object value, Level level) {
		if (OPERATION_NAME.equals(name) ||
				GLoggerConfig.getInstance().getLogContextName().equals(name) ||
				OPERATION_TOOK.equals(name) ||
				OPERATION_STATUS.equals(name)) {
			name = "_" + name;
		}
		this.fields.put(name, new SimpleEntry<>(level, value));
		return this;
	}

	/**
	 * Prints the {"operationStarted": "<i>&lt;operationName&gt;</i>"} log.
	 *
	 * @return This {@link OperationLog} object to chaining methods.
	 */
	OperationLog logStart() {
		this.logger.trace(getFilteredFields(false));
		this.operationStartTime = System.currentTimeMillis();
		this.fields.put(OPERATION_STARTED, new SimpleEntry<>(Level.INFO, new Date().toString()));
		return this;
	}

	/**
	 * Adds {@link #OPERATION_TOOK} fields to the current filters, prints an INFO log and flushes this operation log.
	 *
	 * @see #flush()
	 */
	public void succeed() {
		succeed(this.exitLogLevel, false);
	}

	/**
	 * If {@code verbose} parameter is {@code true} prints all of the fields set on the log regardless from their
	 * minimum level requirement.
	 *
	 * @param verbose Prints all of the fields set on the log regardless of the current log level of the application.
	 *                Default value is {@code false} on {@link #succeed()} and {@link #succeed(Level)}.
	 * @see #succeed()
	 */
	public void succeed(boolean verbose) {
		succeed(this.exitLogLevel, verbose);
	}

	/**
	 * Overrides {@link #exitLogLevel} executes {@link #succeed()} steps.
	 *
	 * @param overrideLevel Log level to override the {@link #exitLogLevel} and print the final log with.
	 * @see #succeed()
	 */
	public void succeed(Level overrideLevel) {
		succeed(overrideLevel, false);
	}

	/**
	 * Overrides {@link #exitLogLevel} executes {@link #succeed()} steps. If {@code verbose} parameter is {@code true}
	 * prints all of the fields set on the log regardless from their minimum level requirement.
	 *
	 * @param overrideLevel Log level to override the {@link #exitLogLevel} and print the final log with.
	 * @param verbose       Prints all of the fields set on the log regardless of the current log level of the
	 *                      application. Default value is {@code false} on {@link #succeed()} and {@link
	 *                      #succeed(Level)}.
	 * @see #succeed()
	 */
	public void succeed(Level overrideLevel, boolean verbose) {
		if (!this.flushed) {
			calculateElapsedTime();
			this.fields.put(OPERATION_STATUS, new SimpleEntry<>(INFO, "succeeded"));
			Map<String, Object> fields = getFilteredFields(verbose);
			if (TRACE.intLevel() == overrideLevel.intLevel()) {
				this.logger.trace(fields);
			} else if (DEBUG.intLevel() == overrideLevel.intLevel()) {
				this.logger.debug(fields);
			} else {
				this.logger.info(fields);
			}
			flush();
		}
	}

	/**
	 * Adds {@link #OPERATION_TOOK} fields to the current filters, prints a WARN log and flushes this operation log.
	 *
	 * @see #flush()
	 */
	public void warn() {
		warn(null, false);
	}

	/**
	 * If {@code verbose} parameter is {@code true} prints all of the fields set on the log regardless from their
	 * minimum level requirement.
	 *
	 * @param verbose Prints all of the fields set on the log regardless of the current log level of the application.
	 *                Default value is {@code false} on {@link #warn()}.
	 */
	public void warn(boolean verbose) {
		warn(null, verbose);
	}

	/**
	 * Adds {@link #OPERATION_TOOK} fields to the current filters, prints a WARN log with given throwable and flushes
	 * this operation log.
	 *
	 * @param t Error or Exception to add the stacktrace to the log.
	 * @see #flush()
	 */
	public void warn(Throwable t) {
		warn(t, false);
	}

	/**
	 * If {@code verbose} parameter is {@code true} prints all of the fields set on the log regardless from their
	 * minimum level requirement.
	 *
	 * @param t       Error or Exception to add the stacktrace to the log.
	 * @param verbose Prints all of the fields set on the log regardless of the current log level of the application.
	 *                Default value is {@code false} on {@link #warn()}.
	 */
	public void warn(Throwable t, boolean verbose) {
		if (!this.flushed) {
			calculateElapsedTime();
			this.fields.put(OPERATION_STATUS, new SimpleEntry<>(INFO, "failed"));
			this.logger.warn(getFilteredFields(verbose), t);
			flush();
		}
	}

	/**
	 * Adds {@link #OPERATION_TOOK} fields to the current filters, prints an ERROR log and flushes this operation log.
	 *
	 * @see #flush()
	 */
	public void fail() {
		fail(null, false);
	}

	/**
	 * If {@code verbose} parameter is {@code true} prints all of the fields set on the log regardless from their
	 * minimum level requirement.
	 *
	 * @param verbose Prints all of the fields set on the log regardless of the current log level of the application.
	 *                Default value is {@code false} on {@link #fail()}.
	 * @see #fail()
	 */
	public void fail(boolean verbose) {
		fail(null, verbose);
	}

	/**
	 * Adds {@link #OPERATION_TOOK} fields to the current filters, prints an ERROR log with given throwable and flushes
	 * this operation log.
	 *
	 * @param t Error or Exception to add the stacktrace to the log.
	 * @see #flush()
	 */
	public void fail(Throwable t) {
		fail(t, false);
	}

	/**
	 * If {@code verbose} parameter is {@code true} prints all of the fields set on the log regardless from their
	 * minimum level requirement.
	 *
	 * @param t       Error or Exception to add the stacktrace to the log.
	 * @param verbose Prints all of the fields set on the log regardless of the current log level of the application.
	 *                Default value is {@code false} on {@link #fail()}.
	 * @see #fail()
	 */
	public void fail(Throwable t, boolean verbose) {
		if (!this.flushed) {
			calculateElapsedTime();
			this.fields.put(OPERATION_STATUS, new SimpleEntry<>(INFO, "failed"));
			this.logger.error(getFilteredFields(verbose), t);
			flush();
		}
	}

	/**
	 * Adds {@link #OPERATION_TOOK} fields to the current filters, prints an FATAL log and flushes this operation log.
	 *
	 * @see #flush()
	 */
	public void fatal() {
		fatal(false);
	}

	/**
	 * If {@code verbose} parameter is {@code true} prints all the fields set on the log regardless from their
	 * minimum level requirement.
	 *
	 * @param verbose Prints all the fields set on the log regardless of the current log level of the application.
	 *                Default value is {@code false} on {@link #fatal()}.
	 * @see #fatal()
	 */
	public void fatal(boolean verbose) {
		if (!this.flushed) {
			calculateElapsedTime();
			this.fields.put(OPERATION_STATUS, new SimpleEntry<>(INFO, "failed"));
			this.logger.fatal(getFilteredFields(verbose));
			flush();
		}
	}

	/**
	 * Returns the context id of this {@link OperationLog} instance as String.
	 *
	 * @return Randomly generated context id of the log as String.
	 */
	public String getContextId() {
		Entry<Level, Object> contextObj = this.fields.get(GLoggerConfig.getInstance().getLogContextName());
		return contextObj != null ? contextObj.getValue().toString() : "";
	}

	/**
	 * Filters the {@link #fields} on this log with their respective {@link Level}s. If the of the field is lower than
	 * the current log level of the application, package, class, etc. it will be filtered out and will not be present on
	 * the map this method produces. <br> With the {@code verbose} fields, this behaviour can be overridden and all
	 * fields can be forced to get passed the filter.
	 *
	 * @param verbose Puts all of the fields set on the log regardless of the current log level of the application.
	 * @return A map containing filtered log values and their names.
	 */
	private Map<String, Object> getFilteredFields(boolean verbose) {
		HashMap<String, Object> reducedMap = new HashMap<>();
		Level currentLevel = this.logger.getLevel();

		this.fields.forEach((fieldEntry, fieldValue) -> {
			if (verbose || fieldValue.getKey().compareTo(currentLevel) <= 0) {
				reducedMap.put(fieldEntry, fieldValue.getValue());
			}
		});

		return reducedMap;
	}

	/**
	 * Calculates total amount of milliseconds took to complete the operation being logged and adds the {@link
	 * #OPERATION_TOOK} field to the fields.
	 *
	 * @see #fields
	 */
	void calculateElapsedTime() {
		this.fields.put(OPERATION_TOOK, new SimpleEntry<>(INFO, System.currentTimeMillis() - this.operationStartTime));
	}

	/**
	 * Flushes this operation log by clearing its fields and clearing the reference of its logger. Calling one of {@link
	 * #succeed()}, {@link #warn()}, {@link #warn(Throwable)}, {@link #fail()}, or {@link #fail(Throwable)} will flush
	 * the operation log. Once flushed, an operation log class cannot be used again.
	 */
	void flush() {
		this.fields.clear();
		this.logger = null;
		this.flushed = true;
	}

	/**
	 * @see #flushed
	 */
	public boolean isFlushed() {
		return flushed;
	}
}
