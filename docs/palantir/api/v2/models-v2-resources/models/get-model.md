`GET /api/v2/models/{modelRid}`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Retrieves a Model by its Resource Identifier (RID).

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

**Model**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Model` | object | 是 | 示例: `{"rid":"ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9"}` |
| `Model.rid` | string | 是 | The Resource Identifier (RID) of a Model.<br>示例: `ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9` |

```json
{
  "rid": "ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `ModelNotFound` | The given Model could not be found. |
