`GET /api/v2/models/modelStudios/{modelStudioRid}`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Gets details about a Model Studio by its RID.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:models-read`.

**OAuth2 scopes**: `api:models-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `modelStudioRid` | string | 是 | The Resource Identifier (RID) of a Model Studio.<br>示例: `ri.models.main.model-studio.a1b2c3d4-e5f6-7890-abcd-ef1234567890` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**ModelStudio**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ModelStudio` | object | 是 | 示例: `{"createdTime":"2003-05-06T12:34:56.789Z","rid":"ri.models.main.model-studio.a1b2c3d4-e5f6-7890-abcd-ef1234567890","folderRid":"ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791"}` |
| `ModelStudio.rid` | string | 是 | The Resource Identifier (RID) of a Model Studio.<br>示例: `ri.models.main.model-studio.a1b2c3d4-e5f6-7890-abcd-ef1234567890` |
| `ModelStudio.folderRid` | string | 是 | The parent folder containing this Model Studio.<br>示例: `ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791` |
| `ModelStudio.createdTime` | string | 是 | The time at which the resource was created.<br>示例: `2003-05-06T12:34:56.789Z` |

```json
{
  "createdTime": "2003-05-06T12:34:56.789Z",
  "rid": "ri.models.main.model-studio.a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "folderRid": "ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `ModelStudioNotFound` | The given ModelStudio could not be found. |
