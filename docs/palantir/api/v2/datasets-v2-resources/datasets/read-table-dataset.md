`GET /api/v2/datasets/{datasetRid}/readTable`

Gets the content of a dataset as a table in the specified format.

This endpoint currently does not support views (virtual datasets composed of other datasets).


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:datasets-read`.

**OAuth2 scopes**: `api:datasets-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `branchName` | string | 否 | The name of the Branch.<br>示例: `master` |
| `startTransactionRid` | string | 否 | The Resource Identifier (RID) of the start Transaction.<br>示例: `ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4` |
| `endTransactionRid` | string | 否 | The Resource Identifier (RID) of the end Transaction.<br>示例: `ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4` |
| `format` | enum | 是 | The export format. Must be `ARROW` or `CSV`.<br>示例: `CSV` |
| `columns` | list<string> | 否 | A subset of the dataset columns to include in the result. Defaults to all columns.<br>示例: `["id","firstName","lastName"]` |
| `rowLimit` | integer | 否 | A limit on the number of rows to return. Note that row ordering is non-deterministic. |

## Response

**body**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `body` | string | 是 | — |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `ColumnTypesNotSupported` | The dataset contains column types that are not supported. |
| PERMISSION_DENIED | `ReadTableDatasetPermissionDenied` | The provided token does not have permission to read the given dataset as a table. |
| INTERNAL | `ReadTableError` | An error occurred while reading the table. Refer to the message for more details. |
| INVALID_ARGUMENT | `ReadTableRowLimitExceeded` | The request to read the table generates a result that exceeds the allowed number of rows. For datasets not<br>stored as Parquet there is a limit of 1 million rows. For datasets stored as Parquet there is no limit. |
| TIMEOUT | `ReadTableTimeout` | The request to read the table timed out. |
| INVALID_ARGUMENT | `DatasetReadNotSupported` | The dataset does not support being read. |
| NOT_FOUND | `SchemaNotFound` | A schema could not be found for the given dataset and branch, or the client token does not have access to it. |
| INVALID_ARGUMENT | `InvalidParameterCombination` | The given parameters are individually valid but cannot be used in the given combination. |
