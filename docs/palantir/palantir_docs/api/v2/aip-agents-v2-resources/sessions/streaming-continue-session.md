`POST /api/v2/aipAgents/agents/{agentRid}/sessions/{sessionRid}/streamingContinue`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Continue a conversation session with an Agent, or add the first exchange to a session after creation.
Adds a new exchange to the session with the provided inputs, and generates a response from the Agent.
Returns a stream of the Agent response text (formatted using markdown) for clients to consume as the response is generated.
On completion of the streamed response, clients can load the full details of the exchange that was added to the session by reloading the session content.
Streamed exchanges also support cancellation; see `cancel` for details.
Concurrent requests to continue the same session are not supported.
Clients should wait to receive a response, or cancel the in-progress exchange, before sending the next message.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:aip-agents-write`.

**OAuth2 scopes**: `api:aip-agents-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `agentRid` | string | 是 | An RID identifying an Agent created in [AIP Chatbot Studio](/docs/foundry/chatbot-studio/overview/).<br>示例: `ri.aip-agents..agent.732cd5b4-7ca7-4219-aabb-6e976faf63b1` |
| `sessionRid` | string | 是 | The Resource Identifier (RID) of the conversation session.<br>示例: `ri.aip-agents..session.292db3b2-b653-4de6-971c-7e97a7b881d6` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
{
  "sessionTraceId": "12345678-1234-5678-1234-123456789abc",
  "messageId": "00f8412a-c29d-4063-a417-8052825285a5",
  "userInput": {
    "text": "What is the status of my order?"
  },
  "parameterInputs": {
    "currentCustomerOrders": {
      "type": "objectSet",
      "ontology": "example-ontology",
      "objectSet": {
        "type": "filter",
        "objectSet": {
          "type": "base",
          "objectType": "customerOrder"
        },
        "where": {
          "type": "eq",
          "field": "customerId",
          "value": "123abc"
        }
      }
    }
  }
}
```

## Response

**body**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `body` | string | 是 | — |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `ObjectTypeIdsNotFound` | Some object types are configured for use by the Agent but could not be found.<br>The object types either do not exist or the client token does not have access.<br>Object types can be checked by listing available object types through the API, or searching in [Ontology Manager](/docs/foundry/ontology-manager/overview/). |
| NOT_FOUND | `ObjectTypeRidsNotFound` | Some object types are configured for use by the Agent but could not be found.<br>The object types either do not exist or the client token does not have access.<br>Object types can be checked by listing available object types through the API, or searching in [Ontology Manager](/docs/foundry/ontology-manager/overview/). |
| NOT_FOUND | `FunctionLocatorNotFound` | The specified function locator is configured for use by the Agent but could not be found.<br>The function type or version may not exist or the client token does not have access. |
| INVALID_ARGUMENT | `InvalidParameter` | The provided application variable is not valid for the Agent for this session.<br>Check the available application variables for the Agent under the `parameters` property, and version through the API with `getAgent`, or in AIP Chatbot Studio.<br>The Agent version used for the session can be checked through the API with `getSession`. |
| INVALID_ARGUMENT | `InvalidParameterType` | The provided value does not match the expected type for the application variable configured on the Agent for this session.<br>Check the available application variables for the Agent under the `parameters` property, and version through the API with `getAgent`, or in AIP Chatbot Studio.<br>The Agent version used for the session can be checked through the API with `getSession`. |
| INVALID_ARGUMENT | `SessionTraceIdAlreadyExists` | The provided trace ID already exists for the session and cannot be reused. |
| NOT_FOUND | `OntologyEntitiesNotFound` | Some ontology types are configured for use by the Agent but could not be found.<br>The types either do not exist or the client token does not have access.<br>Object types and their link types can be checked by listing available object/link types through the API, or searching in [Ontology Manager](/docs/foundry/ontology-manager/overview/). |
| INVALID_ARGUMENT | `UnsupportedLanguageModelRid` | The Agent is configured with a language model that is not supported or could not be resolved.<br>This can surface at runtime if the model was deprecated or is not accessible to the calling token.<br>Update the Agent's language model in AIP Chatbot Studio. |
| INVALID_ARGUMENT | `ActionTypeNotFound` | An action tool configured on the Agent references an action type that could not be found.<br>This can surface at runtime if the action type was deleted or is not accessible to the calling token.<br>Verify the action type exists and is accessible, then review the Agent's tools in AIP Chatbot Studio. |
| PERMISSION_DENIED | `StreamingContinueSessionPermissionDenied` | Could not streamingContinue the Session. |
| NOT_FOUND | `SessionNotFound` | The given Session could not be found. |
| NOT_FOUND | `AgentNotFound` | The given Agent could not be found. |
