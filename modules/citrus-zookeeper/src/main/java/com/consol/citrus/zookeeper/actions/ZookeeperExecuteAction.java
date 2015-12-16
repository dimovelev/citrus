/*
 * Copyright 2006-2015 the original author or authors.
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

package com.consol.citrus.zookeeper.actions;

import com.consol.citrus.actions.AbstractTestAction;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.zookeeper.client.ZookeeperClient;
import com.consol.citrus.zookeeper.command.ZookeeperCommand;
import com.consol.citrus.exceptions.CitrusRuntimeException;
import com.consol.citrus.exceptions.ValidationException;
import com.consol.citrus.message.DefaultMessage;
import com.consol.citrus.validation.json.JsonMessageValidationContext;
import com.consol.citrus.validation.json.JsonTextMessageValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

/**
 * Executes zookeeper command with given zookeeper client implementation. Possible command result is stored within command object.
 *
 * @author Martin Maher
 * @since 2.5
 */
public class ZookeeperExecuteAction extends AbstractTestAction {

    @Autowired(required = false)
    @Qualifier("zookeeperClient")
    /** Zookeeper client instance  */
    private ZookeeperClient zookeeperClient = new ZookeeperClient();

    /** Zookeeper command to execute */
    private ZookeeperCommand command;

    /** Expected command result for validation */
    private String expectedCommandResult;

    @Autowired(required = false)
    @Qualifier("zookeeperCommandResultMapper")
    /** JSON data binding */
    private ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private JsonTextMessageValidator jsonTextMessageValidator;

    /** Logger */
    private static Logger log = LoggerFactory.getLogger(ZookeeperExecuteAction.class);

    /**
     * Default constructor.
     */
    public ZookeeperExecuteAction() {
        setName("zookeeper-execute");
    }

    @Override
    public void doExecute(TestContext context) {
        try {
            if (log.isDebugEnabled()) {
                log.debug(String.format("Executing zookeeper command '%s'", command.getName()));
            }
            command.execute(zookeeperClient, context);

            validateCommandResult(command, context);

            log.info(String.format("Zookeeper command execution successful: '%s'", command.getName()));
        } catch (CitrusRuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new CitrusRuntimeException("Unable to perform zookeeper command", e);
        }
    }

    /**
     * Validate command results.
     * @param command
     * @param context
     */
    private void validateCommandResult(ZookeeperCommand command, TestContext context) {
        if (log.isDebugEnabled()) {
            log.debug("Starting Zookeeper command result validation");
        }

        if (StringUtils.hasText(expectedCommandResult)) {
            if (command.getCommandResult() == null) {
                throw new ValidationException("Missing Zookeeper command result");
            }

            try {
                String commandResultJson = jsonMapper.writeValueAsString(command.getCommandResult());
                JsonMessageValidationContext validationContext = new JsonMessageValidationContext();
                jsonTextMessageValidator.validateMessage(new DefaultMessage(commandResultJson), new DefaultMessage(expectedCommandResult), context, validationContext);
                log.info("Zookeeper command result validation successful - all values OK!");
            } catch (JsonProcessingException e) {
                throw new CitrusRuntimeException(e);
            }
        }

        if (command.getResultCallback() != null) {
            command.getResultCallback().doWithCommandResult(command.getCommandResult(), context);
        }
    }

    /**
     * Gets the zookeeper command to execute.
     * @return
     */
    public ZookeeperCommand getCommand() {
        return command;
    }

    /**
     * Sets zookeeper command to execute.
     * @param command
     * @return
     */
    public ZookeeperExecuteAction setCommand(ZookeeperCommand command) {
        this.command = command;
        return this;
    }

    /**
     * Gets the zookeeper client.
     * @return
     */
    public ZookeeperClient getZookeeperClient() {
        return zookeeperClient;
    }

    /**
     * Sets the zookeeper client.
     * @param zookeeperClient
     */
    public ZookeeperExecuteAction setZookeeperClient(ZookeeperClient zookeeperClient) {
        this.zookeeperClient = zookeeperClient;
        return this;
    }

    /**
     * Gets the expected command result data.
     * @return
     */
    public String getExpectedCommandResult() {
        return expectedCommandResult;
    }

    /**
     * Sets the expected command result data.
     * @param expectedCommandResult
     */
    public ZookeeperExecuteAction setExpectedCommandResult(String expectedCommandResult) {
        this.expectedCommandResult = expectedCommandResult;
        return this;
    }

    /**
     * Sets the JSON object mapper.
     * @param jsonMapper
     */
    public ZookeeperExecuteAction setJsonMapper(ObjectMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
        return this;
    }
}
