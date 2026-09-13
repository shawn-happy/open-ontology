`POST /api/v2/models/modelStudios`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Creates a new Model Studio.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:models-write`.

**OAuth2 scopes**: `api:models-write`

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
{
  "parentFolderRid": "ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791"
}
```

## Response

**ModelStudio**

The created ModelStudio

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ModelStudio` | object | 是 | The created ModelStudio<br>示例: `{"createdTime":"2003-05-06T12:34:56.789Z","rid":"ri.models.main.model-studio.a1b2c3d4-e5f6-7890-abcd-ef1234567890","folderRid":"ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791"}` |
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
| CONFLICT | `ResourceNameAlreadyExists` | The provided resource name is already in use by another resource in the same folder. |
| INVALID_ARGUMENT | `InvalidDisplayName` | The display name of a resource should not be exactly `.` or `..`, contain a forward slash `/` and must be<br>less than or equal to 700 characters. |
| INVALID_ARGUMENT | `InvalidModelStudioCreateRequest` | The request to create a Model Studio contains invalid arguments. |
| PERMISSION_DENIED | `CreateModelStudioPermissionDenied` | Could not create the ModelStudio. |
