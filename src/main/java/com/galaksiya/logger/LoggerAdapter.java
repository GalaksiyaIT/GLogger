package com.galaksiya.logger;

import org.apache.logging.log4j.Level;

import java.util.Map;

/**
 * Logger adapters are enable the use of different logging libraries. This interface can be implemented to implement new
 * logging methods.
 *
 * @author Berkay Akdal
 * @version 1.0.0, 02,04,2019
 * @since 2.0.0
 */
interface LoggerAdapter {

	void debug(String message, Object... params);

	void debug(Map<String, Object> map);

	void debug(String message, Throwable throwable, Object... params);

	void error(String message, Object... params);

	void error(String message, Throwable throwable, Object... params);

	void error(Map<String, Object> map);

	void error(Map<String, Object> map, Throwable t);

	void fatal(String message, Object... params);

	void fatal(String message, Throwable throwable, Object... params);

	void fatal(Map<String, Object> map);

	void info(String message, Object... params);

	void info(String message, Throwable throwable, Object... params);

	void info(Map<String, Object> map);

	void trace(String message, Object... params);

	void trace(String message, Throwable throwable, Object... params);

	void trace(Map<String, Object> map);

	void warn(String message, Object... params);

	void warn(String message, Throwable throwable, Object... params);

	void warn(Map<String, Object> map);

	void warn(Map<String, Object> map, Throwable t);

	Level getLevel();
}
