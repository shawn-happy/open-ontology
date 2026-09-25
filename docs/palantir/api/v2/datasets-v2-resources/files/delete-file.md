`DELETE /api/v2/datasets/{datasetRid}/files/{filePath}`

Deletes a File from a Dataset. By default the file is deleted in a new transaction on the default 
branch - `master` for most enrollments. The file will still be visible on historical views.
#### Advanced Usage
See [Datasets Core Concepts](/docs/foundry/data-integration/datasets/) for details on using branches and transactions.
To **delete a File from a specific Branch** specify the Branch's name as `branchName`. A new delete Transaction 
will be created and committed on this branch.
To **delete a File using a manually opened Transaction**, specify the Transaction's resource identifier 
as `transactionRid`. The transaction must be of type `DELETE`. This is useful for deleting multiple files in a
single transaction. See [createTransaction](/docs/foundry/api/datasets-resources/transactions/create-transaction/) to 
open a transaction.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:datasets-write`.

**OAuth2 scopes**: `api:datasets-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `filePath` | string | 是 | The path to a File within Foundry. Paths are relative and must not start with a leading slash.<br>Examples: `my-file.txt`, `path/to/my-file.jpg`, `dataframe.snappy.parquet`.<br>示例: `My Folder/my-file.csv` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `branchName` | string | 否 | The name of the Branch on which to delete the File. Defaults to `master` for most enrollments.<br>示例: `master` |
| `transactionRid` | string | 否 | The Resource Identifier (RID) of the open delete Transaction on which to delete the File.<br>示例: `ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4` |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `BranchNotFound` | The requested branch could not be found, or the client token does not have access to it. |
| NOT_FOUND | `FileNotFoundOnBranch` | The requested file could not be found on the given branch, or the client token does not have access to it. |
| CONFLICT | `OpenTransactionAlreadyExists` | A transaction is already open on this dataset and branch. A branch of a dataset can only have one open transaction at a time. |
| INVALID_ARGUMENT | `InvalidParameterCombination` | The given parameters are individually valid but cannot be used in the given combination. |
| NOT_FOUND | `DatasetNotFound` | The requested dataset could not be found, or the client token does not have access to it. |
| NOT_FOUND | `TransactionNotFound` | The requested transaction could not be found on the dataset, or the client token does not have access to it. |
| NOT_FOUND | `FileNotFoundOnTransactionRange` | The requested file could not be found on the given transaction range, or the client token does not have access to it. |
| INVALID_ARGUMENT | `InvalidTransactionType` | The given transaction type is not valid. Valid transaction types are `SNAPSHOT`, `UPDATE`, `APPEND`, and `DELETE`. |
| INVALID_ARGUMENT | `TransactionNotOpen` | The given transaction is not open. |
| PERMISSION_DENIED | `CreateTransactionPermissionDenied` | The provided token does not have permission to create a transaction on this dataset. |
| PERMISSION_DENIED | `AbortTransactionPermissionDenied` | The provided token does not have permission to abort the given transaction on the given dataset. |
| PERMISSION_DENIED | `CommitTransactionPermissionDenied` | The provided token does not have permission to commit the given transaction on the given dataset. |
| INVALID_ARGUMENT | `InvalidBranchName` | The requested branch name cannot be used. Branch names cannot be empty and must not look like RIDs or UUIDs. |
| PERMISSION_DENIED | `DeleteFilePermissionDenied` | Could not delete the File. |
