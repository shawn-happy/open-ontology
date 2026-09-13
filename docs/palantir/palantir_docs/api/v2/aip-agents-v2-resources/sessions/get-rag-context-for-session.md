`PUT /api/v2/aipAgents/agents/{agentRid}/sessions/{sessionRid}/ragContext`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Retrieve relevant [context](/docs/foundry/chatbot-studio/core-concepts/#retrieval-context) for a user message from the data sources configured for the session.
This allows clients to pre-retrieve context for a user message before sending it to the Agent with the `contextsOverride` option when continuing a session, to allow any pre-processing of the context before sending it to the Agent.


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
  "userInput": {
    "text": "What is the status of my order?"
  },
  "parameterInputs": {
    "customerName": {
      "type": "string",
      "value": "Titan Technologies"
    }
  }
}
```

## Response

**AgentSessionRagContextResponse**

Context retrieved from an Agent's configured context data sources which was relevant to the supplied user message.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `AgentSessionRagContextResponse` | object | 是 | Context retrieved from an Agent's configured context data sources which was relevant to the supplied user message.<br>示例: `{"functionRetrievedContexts":[{"functionVersion":"1.2.3","functionRid":"ri.function-registry.main.function.8cb2d957-f0e6-4e0c-81de-e701bd54b18c"}],"objectContexts":[{"objectRids":["ri.phonograph2-objects.main.object.48668bf6-8878-48d2-b8f8-f0017593feb5"],"propertyTypeRids":["ri.ontology.main.property.7899aeb4-a389-4f2e-a0fd-e7193a4f6cb1"]}]}` |
| `AgentSessionRagContextResponse.objectContexts` | list<ObjectContext> | 否 | — |
| `AgentSessionRagContextResponse.objectContexts.ObjectContext` | object | 是 | Details of relevant retrieved object instances for a user's message to include as additional context in the prompt to the Agent. |
| `AgentSessionRagContextResponse.objectContexts.ObjectContext.objectRids` | list<ObjectRid> | 否 | The RIDs of the relevant object instances to include in the prompt. |
| `AgentSessionRagContextResponse.objectContexts.ObjectContext.objectRids.ObjectRid` | string | 是 | The unique resource identifier of an object, useful for interacting with other Foundry APIs. |
| `AgentSessionRagContextResponse.objectContexts.ObjectContext.propertyTypeRids` | list<PropertyTypeRid> | 否 | The RIDs of the property types for the given objects to include in the prompt. |
| `AgentSessionRagContextResponse.objectContexts.ObjectContext.propertyTypeRids.PropertyTypeRid` | string | 是 | The unique resource identifier of a property. |
| `AgentSessionRagContextResponse.functionRetrievedContexts` | list<FunctionRetrievedContext> | 否 | — |
| `AgentSessionRagContextResponse.functionRetrievedContexts.FunctionRetrievedContext` | object | 是 | Context retrieved from running a function to include as additional context in the prompt to the Agent. |
| `AgentSessionRagContextResponse.functionRetrievedContexts.FunctionRetrievedContext.functionRid` | string | 是 | The unique resource identifier of a Function, useful for interacting with other Foundry APIs.<br>示例: `ri.function-registry.main.function.8cb2d957-f0e6-4e0c-81de-e701bd54b18c` |
| `AgentSessionRagContextResponse.functionRetrievedContexts.FunctionRetrievedContext.functionVersion` | string | 是 | The version of the given Function, written `<major>.<minor>.<patch>-<tag>`, where `-<tag>` is optional.<br>Examples: `1.2.3`, `1.2.3-rc1`.<br>示例: `1.2.3` |
| `AgentSessionRagContextResponse.functionRetrievedContexts.FunctionRetrievedContext.retrievedPrompt` | string | 是 | String content returned from a context retrieval function. |

```json
{
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
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `ObjectTypeIdsNotFound` | Some object types are configured for use by the Agent but could not be found.<br>The object types either do not exist or the client token does not have access.<br>Object types can be checked by listing available object types through the API, or searching in [Ontology Manager](/docs/foundry/ontology-manager/overview/). |
| NOT_FOUND | `ObjectTypeRidsNotFound` | Some object types are configured for use by the Agent but could not be found.<br>The object types either do not exist or the client token does not have access.<br>Object types can be checked by listing available object types through the API, or searching in [Ontology Manager](/docs/foundry/ontology-manager/overview/). |
| NOT_FOUND | `FunctionLocatorNotFound` | The specified function locator is configured for use by the Agent but could not be found.<br>The function type or version may not exist or the client token does not have access. |
| NOT_FOUND | `OntologyEntitiesNotFound` | Some ontology types are configured for use by the Agent but could not be found.<br>The types either do not exist or the client token does not have access.<br>Object types and their link types can be checked by listing available object/link types through the API, or searching in [Ontology Manager](/docs/foundry/ontology-manager/overview/). |
| PERMISSION_DENIED | `GetRagContextForSessionPermissionDenied` | Could not ragContext the Session. |
| NOT_FOUND | `SessionNotFound` | The given Session could not be found. |
| NOT_FOUND | `AgentNotFound` | The given Agent could not be found. |
