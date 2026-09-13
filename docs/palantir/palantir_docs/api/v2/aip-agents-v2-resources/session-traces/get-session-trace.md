`GET /api/v2/aipAgents/agents/{agentRid}/sessions/{sessionRid}/sessionTraces/{sessionTraceId}`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Get the trace of an Agent response. The trace lists the sequence of steps that an Agent took to arrive at
an answer. For example, a trace may include steps such as context retrieval and tool calls. Clients should
poll this endpoint to check the realtime progress of a response until the trace is completed.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:aip-agents-read`.

**OAuth2 scopes**: `api:aip-agents-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `agentRid` | string | 是 | An RID identifying an Agent created in [AIP Chatbot Studio](/docs/foundry/chatbot-studio/overview/).<br>示例: `ri.aip-agents..agent.732cd5b4-7ca7-4219-aabb-6e976faf63b1` |
| `sessionRid` | string | 是 | The Resource Identifier (RID) of the conversation session.<br>示例: `ri.aip-agents..session.292db3b2-b653-4de6-971c-7e97a7b881d6` |
| `sessionTraceId` | string | 是 | The unique identifier for the trace.<br>示例: `12345678-1234-5678-1234-123456789abc` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**SessionTrace**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `SessionTrace` | object | 是 | 示例: `{"toolCallGroups":[{"toolCalls":[{"toolMetadata":{"name":"Object Query Tool","type":"FUNCTION"},"input":{"thought":"I need to find the customer with the name 'Titan Technologies'."}}]}],"id":"12345678-1234-5678-1234-123456789abc","contexts":{"functionRetrievedContexts":[{"functionVersion":"1.2.3","functionRid":"ri.function-registry.main.function.8cb2d957-f0e6-4e0c-81de-e701bd54b18c"}],"objectContexts":[{"objectRids":["ri.phonograph2-objects.main.object.48668bf6-8878-48d2-b8f8-f0017593feb5"],"propertyTypeRids":["ri.ontology.main.property.7899aeb4-a389-4f2e-a0fd-e7193a4f6cb1"]}]},"status":"IN_PROGRESS"}` |
| `SessionTrace.id` | string | 是 | The unique identifier for the trace.<br>示例: `12345678-1234-5678-1234-123456789abc` |
| `SessionTrace.status` | enum | 是 | This indicates whether the Agent has finished generating the final response. Clients should keep polling<br>the `getSessionTrace` endpoint until the status is `COMPLETE`.<br>示例: `IN_PROGRESS` |
| `SessionTrace.contexts` | object | 否 | Any additional context which was provided by the client or retrieved automatically by the agent, grouped<br>by context type. Empty if no additional context was provided or configured to be automatically<br>retrieved. A present SessionExchangeContexts object with empty lists indicates that context retrieval<br>was attempted but no context was found.<br>Note that this field will only be populated once the response generation has completed.<br>示例: `{"functionRetrievedContexts":[{"functionVersion":"1.2.3","functionRid":"ri.function-registry.main.function.8cb2d957-f0e6-4e0c-81de-e701bd54b18c"}],"objectContexts":[{"objectRids":["ri.phonograph2-objects.main.object.48668bf6-8878-48d2-b8f8-f0017593feb5"],"propertyTypeRids":["ri.ontology.main.property.7899aeb4-a389-4f2e-a0fd-e7193a4f6cb1"]}]}` |
| `SessionTrace.contexts.objectContexts` | list<ObjectContext> | 否 | Relevant object context for the user's message that was included in the prompt to the Agent. |
| `SessionTrace.contexts.objectContexts.ObjectContext` | object | 是 | Details of relevant retrieved object instances for a user's message to include as additional context in the prompt to the Agent. |
| `SessionTrace.contexts.objectContexts.ObjectContext.objectRids` | list<ObjectRid> | 否 | The RIDs of the relevant object instances to include in the prompt. |
| `SessionTrace.contexts.objectContexts.ObjectContext.objectRids.ObjectRid` | string | 是 | The unique resource identifier of an object, useful for interacting with other Foundry APIs. |
| `SessionTrace.contexts.objectContexts.ObjectContext.propertyTypeRids` | list<PropertyTypeRid> | 否 | The RIDs of the property types for the given objects to include in the prompt. |
| `SessionTrace.contexts.objectContexts.ObjectContext.propertyTypeRids.PropertyTypeRid` | string | 是 | The unique resource identifier of a property. |
| `SessionTrace.contexts.functionRetrievedContexts` | list<FunctionRetrievedContext> | 否 | Context retrieved from running a function that was included as additional context in the prompt to the Agent. |
| `SessionTrace.contexts.functionRetrievedContexts.FunctionRetrievedContext` | object | 是 | Context retrieved from running a function to include as additional context in the prompt to the Agent. |
| `SessionTrace.contexts.functionRetrievedContexts.FunctionRetrievedContext.functionRid` | string | 是 | The unique resource identifier of a Function, useful for interacting with other Foundry APIs.<br>示例: `ri.function-registry.main.function.8cb2d957-f0e6-4e0c-81de-e701bd54b18c` |
| `SessionTrace.contexts.functionRetrievedContexts.FunctionRetrievedContext.functionVersion` | string | 是 | The version of the given Function, written `<major>.<minor>.<patch>-<tag>`, where `-<tag>` is optional.<br>Examples: `1.2.3`, `1.2.3-rc1`.<br>示例: `1.2.3` |
| `SessionTrace.contexts.functionRetrievedContexts.FunctionRetrievedContext.retrievedPrompt` | string | 是 | String content returned from a context retrieval function. |
| `SessionTrace.toolCallGroups` | list<ToolCallGroup> | 否 | List of tool call groups that were triggered at the same point in the trace for the agent response<br>generation. The groups are returned in the same order as they were triggered by the agent. |
| `SessionTrace.toolCallGroups.ToolCallGroup` | object | 是 | List of tool calls that were triggered at the same point in the trace for the agent response generation. |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls` | list<ToolCall> | 否 | — |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall` | object | 是 | A tool call with its input and output. |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall.toolMetadata` | object | 是 | Details about the tool that was called, including the name and type of the tool.<br>示例: `{"name":"Object Query Tool","type":"FUNCTION"}` |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall.toolMetadata.name` | string | 是 | The name of the tool that was called, as configured on the Agent.<br>示例: `Object Query Tool` |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall.toolMetadata.type` | enum | 是 | The type of the tool that was called.<br>示例: `FUNCTION` |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall.input` | object | 是 | Input parameters for a tool call.<br>示例: `{"thought":"I need to find the customer with the name 'Titan Technologies'."}` |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall.input.thought` | string | 否 | Any additional message content that the Agent provided for why it chose to call the tool.<br>示例: `I need to find the customer with the name 'Titan Technologies'.` |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall.input.inputs` | map | 否 | — |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall.input.inputs.ToolInputName` | string | 是 | The name of a tool input parameter. |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall.input.inputs.ToolInputValue` | union | 是 | A tool input value, which can be either a string or a Resource Identifier (RID). |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall.input.inputs.ToolInputValue.string` | object | 否 | A string value that was passed as input to a tool. |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall.input.inputs.ToolInputValue.string.value` | string | 是 | 示例: `Titan Technologies` |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall.input.inputs.ToolInputValue.rid` | object | 否 | A Resource Identifier (RID) that was passed as input to a tool. |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall.input.inputs.ToolInputValue.rid.rid` | string | 是 | 示例: `ri.object-set.main.temporary-object-set.8b12d4a9-4cf5-43c4-a612-77e5fc015e02` |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall.output` | union | 否 | Empty if the tool call is in progress. |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall.output.success` | object | 否 | The successful output of a tool call. |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall.output.success.output` | union | 是 | A tool output value, which can be either a string or a Resource Identifier (RID). |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall.output.success.output.string` | object | 否 | A string value that was returned from a tool. |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall.output.success.output.string.value` | string | 是 | 示例: `Titan Technologies` |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall.output.success.output.rid` | object | 否 | A Resource Identifier (RID) value that was returned from a tool. |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall.output.success.output.rid.rid` | string | 是 | 示例: `ri.object-set.main.temporary-object-set.8b12d4a9-4cf5-43c4-a612-77e5fc015e02` |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall.output.failure` | object | 否 | The failed output of a tool call. |
| `SessionTrace.toolCallGroups.ToolCallGroup.toolCalls.ToolCall.output.failure.correctionMessage` | string | 是 | The correction message returned by the tool if the tool call was not successful.<br>This is a message that the tool returned to the Agent, which may be used to correct the<br>Agent's input to the tool.<br>示例: `The order ID must be a string.` |

```json
{
  "toolCallGroups": [
    {
      "toolCalls": [
        {
          "toolMetadata": {
            "name": "Object Query Tool",
            "type": "FUNCTION"
          },
          "input": {
            "thought": "I need to find the customer with the name 'Titan Technologies'."
          }
        }
      ]
    }
  ],
  "id": "12345678-1234-5678-1234-123456789abc",
  "contexts": {
    "functionRetrievedContexts": [
      {
        "functionVersion": "1.2.3",
        "functionRid": "ri.function-registry.main.function.8cb2d957-f0e6-4e0c-81de-e701bd54b18c"
      }
    ],
    "objectContexts": [
      {
        "objectRids": [
          "ri.phonograph2-objects.main.object.48668bf6-8878-48d2-b8f8-f0017593feb5"
        ],
        "propertyTypeRids": [
          "ri.ontology.main.property.7899aeb4-a389-4f2e-a0fd-e7193a4f6cb1"
        ]
      }
    ]
  },
  "status": "IN_PROGRESS"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `SessionTraceNotFound` | The given SessionTrace could not be found. |
| NOT_FOUND | `SessionNotFound` | The given Session could not be found. |
| NOT_FOUND | `AgentNotFound` | The given Agent could not be found. |
