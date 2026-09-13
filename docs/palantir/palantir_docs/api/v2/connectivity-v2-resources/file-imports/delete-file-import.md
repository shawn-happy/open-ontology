`DELETE /api/v2/connectivity/connections/{connectionRid}/fileImports/{fileImportRid}`

Delete the FileImport with the specified RID.
Deleting the file import does not delete the destination dataset but the dataset will no longer
be updated by this import.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:connectivity-file-import-write`.

**OAuth2 scopes**: `api:connectivity-file-import-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `connectionRid` | string | 是 | The Resource Identifier (RID) of a Connection (also known as a source).<br>示例: `ri.magritte..source.c078b71b-92f9-41b6-b0df-3760f411120b` |
| `fileImportRid` | string | 是 | The Resource Identifier (RID) of a FileImport (also known as a batch sync).<br>示例: `ri.magritte..extract.27bb4f2b-63b8-44b8-a579-4e2bd65ba158` |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `DeleteFileImportPermissionDenied` | Could not delete the FileImport. |
| NOT_FOUND | `FileImportNotFound` | The given FileImport could not be found. |
