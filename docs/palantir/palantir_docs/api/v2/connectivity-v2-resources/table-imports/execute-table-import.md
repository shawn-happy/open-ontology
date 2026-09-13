`POST /api/v2/connectivity/connections/{connectionRid}/tableImports/{tableImportRid}/execute`

Executes the TableImport, which runs asynchronously as a [Foundry Build](/docs/foundry/data-integration/builds/).
The returned BuildRid can be used to check the status via the Orchestration API.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:connectivity-table-import-execute`.

**OAuth2 scopes**: `api:connectivity-table-import-execute`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `connectionRid` | string | 是 | The Resource Identifier (RID) of a Connection (also known as a source).<br>示例: `ri.magritte..source.c078b71b-92f9-41b6-b0df-3760f411120b` |
| `tableImportRid` | string | 是 | The Resource Identifier (RID) of a TableImport (also known as a batch sync).<br>示例: `ri.magritte..extract.27bb4f2b-63b8-44b8-a579-4e2bd65ba158` |

## Response

**BuildRid**

The RID of a Build.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `BuildRid` | string | 是 | The RID of a Build.<br>示例: `ri.foundry.main.build.a4386b7e-d546-49be-8a36-eefc355f5c58` |

```text
ri.foundry.main.build.a4386b7e-d546-49be-8a36-eefc355f5c58
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `ExecuteTableImportPermissionDenied` | Could not execute the TableImport. |
| NOT_FOUND | `TableImportNotFound` | The given TableImport could not be found. |
