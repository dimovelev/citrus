/*
 * Copyright 2006-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.zookeeper.command;

import com.consol.citrus.context.TestContext;
import com.consol.citrus.exceptions.CitrusRuntimeException;
import com.consol.citrus.zookeeper.client.ZookeeperClient;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Maher
 * @since 2.5
 */
public class Exists extends AbstractZookeeperCommand<ZookeeperResponse> {

    /**
     * Logger
     */
    private static Logger log = LoggerFactory.getLogger(Exists.class);

    /**
     * Default constructor initializing the command name.
     */
    public Exists() {
        super("zookeeper:exists");
    }

    @Override
    public void execute(ZookeeperClient zookeeperClient, TestContext context) {
        ZookeeperResponse commandResult = new ZookeeperResponse();
        setCommandResult(commandResult);

        String path = this.getParameter("path", context);

        try {
            Stat stat = zookeeperClient.getZooKeeperClient().exists(path, false);
            CommandHelper.parseStatResponse(commandResult,stat);
        } catch (InterruptedException | KeeperException e) {
            throw new CitrusRuntimeException(e);
        }
        log.debug(getCommandResult().toString());
    }
}
