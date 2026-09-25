`POST /api/v2/connectivity/connections/{connectionRid}/updateExportSettings`

Updates the [export settings on the Connection.](/docs/foundry/data-connection/export-overview/#enable-exports-for-source)
Only users with Information Security Officer role can modify the export settings.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:connectivity-connection-write`.

**OAuth2 scopes**: `api:connectivity-connection-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `connectionRid` | string | 是 | The Resource Identifier (RID) of a Connection (also known as a source).<br>示例: `ri.magritte..source.c078b71b-92f9-41b6-b0df-3760f411120b` |

## Request body

```json
{
  "exportSettings": {
    "exportsEnabled": true,
    "exportEnabledWithoutMarkingsValidation": false
  }
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `UpdateExportSettingsForConnectionPermissionDenied` | Could not updateExportSettings the Connection. |
| NOT_FOUND | `ConnectionNotFound` | The given Connection could not be found. |
