/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.action.delete;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.rest.RestStatus;

import java.io.IOException;

/**
 * The response of the delete action.
 *
 * @see org.elasticsearch.action.delete.DeleteRequest
 * @see org.elasticsearch.client.Client#delete(DeleteRequest)
 */
public class DeleteResponse extends DocWriteResponse {

    public DeleteResponse() {

    }

    public DeleteResponse(ShardId shardId, String type, String id, long version, boolean found) {
        super(shardId, type, id, version, toOperation(found));
    }

    public static Operation toOperation(boolean found) {
        return found ? Operation.DELETE: Operation.NOOP;
    }

    /**
     * Returns <tt>true</tt> if a doc was found to delete.
     */
    public boolean isFound() {
        return operation == Operation.DELETE;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
    }

    @Override
    public RestStatus status() {
        return isFound() ? super.status() : RestStatus.NOT_FOUND;
    }

    static final class Fields {
        static final String FOUND = "found";
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.field(Fields.FOUND, isFound());
        super.toXContent(builder, params);
        return builder;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DeleteResponse[");
        builder.append("index=").append(getIndex());
        builder.append(",type=").append(getType());
        builder.append(",id=").append(getId());
        builder.append(",version=").append(getVersion());
        builder.append(",found=").append(isFound());
        builder.append(",operation=").append(getOperation().getLowercase());
        builder.append(",shards=").append(getShardInfo());
        return builder.append("]").toString();
    }
}
