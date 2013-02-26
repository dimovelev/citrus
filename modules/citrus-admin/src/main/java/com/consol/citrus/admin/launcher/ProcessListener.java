/*
 * Copyright 2006-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.admin.launcher;

/**
 * Process listener for receiving start, success, fail, stop and output events for processes launched
 * using the {@link com.consol.citrus.admin.launcher.ProcessLauncher}.
 *
 * @author Martin.Maher@consol.de
 * @version $Id$
 * @since 2012.11.30
 */
public interface ProcessListener {

    /**
     * Invoked on start process event
     *
     * @param processId the id of the process
     */
    void start(String processId);

    /**
     * Invoked on successful completion event
     *
     * @param processId the id of the completed process
     */
    void success(String processId);

    /**
     * Invoked on failed completion event, with the process exit code
     *
     * @param processId the id of the process
     * @param exitCode the exitcode returned from the process
     */
    void fail(String processId, int exitCode);

    /**
     * Invoked on failed completion event, with the exception that was caught
     *
     * @param processId the id of the process
     * @param e the exception caught within the ProcessLauncher
     */
    void fail(String processId, Exception e);

    /**
     * Invoked on output message event with output data from process
     *
     * @param processId the id of the process
     * @param output
     */
    void output(String processId, String output);
}