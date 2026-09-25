`GET /api/v2/aipAgents/agents/{agentRid}/sessions/{sessionRid}/content`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Get the conversation content for a session between the calling user and an Agent.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:aip-agents-read`.

**OAuth2 scopes**: `api:aip-agents-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `agentRid` | string | 是 | An RID identifying an Agent created in [AIP Chatbot Studio](/docs/foundry/chatbot-studio/overview/).<br>示例: `ri.aip-agents..agent.732cd5b4-7ca7-4219-aabb-6e976faf63b1` |
| `sessionRid` | string | 是 | The Resource Identifier (RID) of the conversation session.<br>示例: `ri.aip-agents..session.292db3b2-b653-4de6-971c-7e97a7b881d6` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**Content**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Content` | object | 是 | 示例: `{"exchanges":[{"result":{"totalTokensUsed":6448,"agentMarkdownResponse":"The status of your order is **In Transit**.","sessionTraceId":"12345678-1234-5678-1234-123456789abc","interruptedOutput":false},"userInput":{"text":"What is the status of my order?"},"contexts":{"functionRetrievedContexts":[{"functionVersion":"1.2.3","functionRid":"ri.function-registry.main.function.8cb2d957-f0e6-4e0c-81de-e701bd54b18c"}],"objectContexts":[{"objectRids":["ri.phonograph2-objects.main.object.48668bf6-8878-48d2-b8f8-f0017593feb5"],"propertyTypeRids":["ri.ontology.main.property.7899aeb4-a389-4f2e-a0fd-e7193a4f6cb1"]}]}}]}` |
| `Content.exchanges` | list<SessionExchange> | 否 | The conversation history for the session, represented as a list of exchanges.<br>Each exchange represents an initiating message from the user and the Agent's response.<br>Exchanges are returned in chronological order, starting with the first exchange. |
| `Content.exchanges.SessionExchange` | object | 是 | Represents an individual exchange between a user and an Agent in a conversation session. |
| `Content.exchanges.SessionExchange.userInput` | object | 是 | The user message that initiated the exchange.<br>示例: `{"text":"What is the status of my order?"}` |
| `Content.exchanges.SessionExchange.userInput.text` | string | 是 | The user message text.<br>示例: `What is the status of my order?` |
| `Content.exchanges.SessionExchange.contexts` | object | 否 | Additional retrieved context that was included in the prompt to the Agent.<br>This may include context that was passed by the client with the user input, or relevant context that was automatically retrieved and added based on available data sources configured on the Agent.<br>Empty if no additional context was included in the prompt.<br>示例: `{"functionRetrievedContexts":[{"functionVersion":"1.2.3","functionRid":"ri.function-registry.main.function.8cb2d957-f0e6-4e0c-81de-e701bd54b18c"}],"objectContexts":[{"objectRids":["ri.phonograph2-objects.main.object.48668bf6-8878-48d2-b8f8-f0017593feb5"],"propertyTypeRids":["ri.ontology.main.property.7899aeb4-a389-4f2e-a0fd-e7193a4f6cb1"]}]}` |
| `Content.exchanges.SessionExchange.contexts.objectContexts` | list<ObjectContext> | 否 | Relevant object context for the user's message that was included in the prompt to the Agent. |
| `Content.exchanges.SessionExchange.contexts.objectContexts.ObjectContext` | object | 是 | Details of relevant retrieved object instances for a user's message to include as additional context in the prompt to the Agent. |
| `Content.exchanges.SessionExchange.contexts.objectContexts.ObjectContext.objectRids` | list<ObjectRid> | 否 | The RIDs of the relevant object instances to include in the prompt. |
| `Content.exchanges.SessionExchange.contexts.objectContexts.ObjectContext.objectRids.ObjectRid` | string | 是 | The unique resource identifier of an object, useful for interacting with other Foundry APIs. |
| `Content.exchanges.SessionExchange.contexts.objectContexts.ObjectContext.propertyTypeRids` | list<PropertyTypeRid> | 否 | The RIDs of the property types for the given objects to include in the prompt. |
| `Content.exchanges.SessionExchange.contexts.objectContexts.ObjectContext.propertyTypeRids.PropertyTypeRid` | string | 是 | The unique resource identifier of a property. |
| `Content.exchanges.SessionExchange.contexts.functionRetrievedContexts` | list<FunctionRetrievedContext> | 否 | Context retrieved from running a function that was included as additional context in the prompt to the Agent. |
| `Content.exchanges.SessionExchange.contexts.functionRetrievedContexts.FunctionRetrievedContext` | object | 是 | Context retrieved from running a function to include as additional context in the prompt to the Agent. |
| `Content.exchanges.SessionExchange.contexts.functionRetrievedContexts.FunctionRetrievedContext.functionRid` | string | 是 | The unique resource identifier of a Function, useful for interacting with other Foundry APIs.<br>示例: `ri.function-registry.main.function.8cb2d957-f0e6-4e0c-81de-e701bd54b18c` |
| `Content.exchanges.SessionExchange.contexts.functionRetrievedContexts.FunctionRetrievedContext.functionVersion` | string | 是 | The version of the given Function, written `<major>.<minor>.<patch>-<tag>`, where `-<tag>` is optional.<br>Examples: `1.2.3`, `1.2.3-rc1`.<br>示例: `1.2.3` |
| `Content.exchanges.SessionExchange.contexts.functionRetrievedContexts.FunctionRetrievedContext.retrievedPrompt` | string | 是 | String content returned from a context retrieval function. |
| `Content.exchanges.SessionExchange.result` | object | 是 | The final result for the exchange.<br>示例: `{"totalTokensUsed":6448,"agentMarkdownResponse":"The status of your order is **In Transit**.","sessionTraceId":"12345678-1234-5678-1234-123456789abc","interruptedOutput":false}` |
| `Content.exchanges.SessionExchange.result.agentMarkdownResponse` | string | 是 | The final text response generated by the Agent. Responses are formatted using markdown.<br>示例: `The status of your order is **In Transit**.` |
| `Content.exchanges.SessionExchange.result.parameterUpdates` | map | 否 | Any updates to application variable values which were generated by the Agent for this exchange.<br>Updates can only be generated for application variables configured with `READ_WRITE` access on the Agent in AIP Chatbot Studio. |
| `Content.exchanges.SessionExchange.result.parameterUpdates.ParameterId` | string | 是 | The unique identifier for a variable configured in the application state of an Agent in [AIP Chatbot Studio](/docs/foundry/chatbot-studio/overview/). |
| `Content.exchanges.SessionExchange.result.parameterUpdates.ParameterValueUpdate` | union | 是 | A value update for an [application variable](/docs/foundry/chatbot-studio/application-state/) generated by the Agent.<br>For `StringParameter` types, this will be the updated string value.<br>For `ObjectSetParameter` types, this will be a Resource Identifier (RID) for the updated object set. |
| `Content.exchanges.SessionExchange.result.parameterUpdates.ParameterValueUpdate.string` | object | 否 | A value passed for `StringParameter` application variable types. |
| `Content.exchanges.SessionExchange.result.parameterUpdates.ParameterValueUpdate.string.value` | string | 是 | 示例: `Titan Technologies` |
| `Content.exchanges.SessionExchange.result.parameterUpdates.ParameterValueUpdate.objectSet` | object | 否 | — |
| `Content.exchanges.SessionExchange.result.parameterUpdates.ParameterValueUpdate.objectSet.value` | string | 是 | — |
| `Content.exchanges.SessionExchange.result.totalTokensUsed` | integer | 否 | Total tokens used to compute the result. Omitted if token usage information is not supported by the model used for the session.<br>示例: `6448` |
| `Content.exchanges.SessionExchange.result.interruptedOutput` | boolean | 是 | True if the exchange was canceled.<br>In that case, the response (if any) was provided by the client as part of the cancellation request rather than by the Agent.<br>示例: `false` |
| `Content.exchanges.SessionExchange.result.sessionTraceId` | string | 是 | The unique identifier for the session trace. The session trace lists the sequence of steps that an Agent<br>takes to arrive at an answer. For example, a trace may include steps such as context retrieval and tool calls.<br>示例: `12345678-1234-5678-1234-123456789abc` |

```json
{
  "exchanges": [
    {
      "result": {
        "totalTokensUsed": 6448,
        "agentMarkdownResponse": "The status of your order is **In Transit**.",
        "sessionTraceId": "12345678-1234-5678-1234-123456789abc",
        "interruptedOutput": false
      },
      "userInput": {
        "text": "What is the status of my order?"
      },
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
      }
    }
  ]
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `ContentNotFound` | The given Content could not be found. |
| NOT_FOUND | `AgentNotFound` | The given Agent could not be found. |
| NOT_FOUND | `SessionNotFound` | The given Session could not be found. |
