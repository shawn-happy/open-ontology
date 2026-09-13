`POST /api/v2/models`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Creates a new Model with no versions.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:models-write`.

**OAuth2 scopes**: `api:models-write`

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
{
  "parentFolderRid": "ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791",
  "name": "House Pricing Model"
}
```

## Response

**Model**

The created Model

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Model` | object | 是 | The created Model<br>示例: `{"rid":"ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9"}` |
| `Model.rid` | string | 是 | The Resource Identifier (RID) of a Model.<br>示例: `ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9` |

```json
{
  "rid": "ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| CONFLICT | `ResourceNameAlreadyExists` | The provided resource name is already in use by another resource in the same folder. |
| INVALID_ARGUMENT | `InvalidDisplayName` | The display name of a resource should not be exactly `.` or `..`, contain a forward slash `/` and must be<br>less than or equal to 700 characters. |
| PERMISSION_DENIED | `CreateModelPermissionDenied` | Could not create the Model. |
