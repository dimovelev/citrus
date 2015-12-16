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
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Martin Maher
 * @since 2.5
 */
public class SetData extends AbstractZookeeperCommand<ZookeeperResponse> {

    /**
     * Logger
     */
    private static Logger log = LoggerFactory.getLogger(SetData.class);

    /**
     * Default constructor initializing the command name.
     */
    public SetData() {
        super("zookeeper:setData");
    }

    @Override
    public void execute(ZookeeperClient zookeeperClient, TestContext context) {
        ZookeeperResponse commandResult = new ZookeeperResponse();
        setCommandResult(commandResult);

        String path = this.getParameter("path", context);
        String data = this.getParameter("data", context);
        int version = Integer.valueOf(this.getParameter("version", context));

        try {
            Stat stat = zookeeperClient.getZooKeeperClient().setData(path, data.getBytes(), version);
            CommandHelper.parseStatResponse(commandResult,stat);
        } catch (KeeperException | InterruptedException e) {
            throw new CitrusRuntimeException(e);
        }
        log.debug(getCommandResult().toString());
    }
}
