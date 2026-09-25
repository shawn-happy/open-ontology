`GET /api/v2/models/{modelRid}/function`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Gets the function for the model.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:models-read`.

**OAuth2 scopes**: `api:models-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `modelRid` | string | 是 | The Resource Identifier (RID) of a Model.<br>示例: `ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**ModelFunction**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ModelFunction` | object | 是 | 示例: `{"apiName":"myModelFunction","ontologyBinding":"ri.ontology.main.ontology.a1b2c3d4-e5f6-7890-abcd-ef1234567890","isRowWise":false,"functionVersion":"0.0.1","displayName":"Core.DisplayName","functionRid":"ri.function-registry.main.function.a1b2c3d4-e5f6-7890-abcd-ef1234567890"}` |
| `ModelFunction.functionRid` | string | 是 | 示例: `ri.function-registry.main.function.a1b2c3d4-e5f6-7890-abcd-ef1234567890` |
| `ModelFunction.functionVersion` | string | 是 | 示例: `0.0.1` |
| `ModelFunction.displayName` | string | 是 | 示例: `Core.DisplayName` |
| `ModelFunction.apiName` | string | 是 | 示例: `myModelFunction` |
| `ModelFunction.isRowWise` | boolean | 是 | 示例: `false` |
| `ModelFunction.ontologyBinding` | string | 否 | The unique Resource Identifier (RID) of the Ontology. To look up your Ontology RID, please use the<br>`List ontologies` endpoint or check the **Ontology Manager**.<br>示例: `ri.ontology.main.ontology.a1b2c3d4-e5f6-7890-abcd-ef1234567890` |

```json
{
  "apiName": "myModelFunction",
  "ontologyBinding": "ri.ontology.main.ontology.a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "isRowWise": false,
  "functionVersion": "0.0.1",
  "displayName": "Core.DisplayName",
  "functionRid": "ri.function-registry.main.function.a1b2c3d4-e5f6-7890-abcd-ef1234567890"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `ModelFunctionNotFound` | The given ModelFunction could not be found. |
| NOT_FOUND | `ModelNotFound` | The given Model could not be found. |
