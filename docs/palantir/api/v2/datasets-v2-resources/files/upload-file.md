`POST /api/v2/datasets/{datasetRid}/files/{filePath}/upload`

Uploads a File to an existing Dataset.
The body of the request must contain the binary content of the file and the `Content-Type` header must be `application/octet-stream`.
By default the file is uploaded to a new transaction on the default branch - `master` for most enrollments.
If the file already exists only the most recent version will be visible in the updated view.
#### Advanced Usage
See [Datasets Core Concepts](/docs/foundry/data-integration/datasets/) for details on using branches and transactions. 
To **upload a file to a specific Branch** specify the Branch's name as `branchName`. A new transaction will 
be created and committed on this branch. By default the TransactionType will be `UPDATE`, to override this
default specify `transactionType` in addition to `branchName`. 
See [createBranch](/docs/foundry/api/datasets-resources/branches/create-branch/) to create a custom branch.
To **upload a file on a manually opened transaction** specify the Transaction's resource identifier as
`transactionRid`. This is useful for uploading multiple files in a single transaction. 
See [createTransaction](/docs/foundry/api/datasets-resources/transactions/create-transaction/) to open a transaction.


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
| `branchName` | string | 否 | The name of the Branch on which to upload the File. Defaults to `master` for most enrollments.<br>示例: `master` |
| `transactionType` | enum | 否 | The type of the Transaction to create when using branchName. Defaults to `UPDATE`.<br>示例: `APPEND` |
| `transactionRid` | string | 否 | The Resource Identifier (RID) of the open Transaction on which to upload the File.<br>示例: `ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4` |

## Request body

## Response

**File**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `File` | object | 是 | 示例: `{"path":"2020/09/30/trades.csv","updatedTime":"2020-09-30T12:34:56.789Z","transactionRid":"ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4"}` |
| `File.path` | string | 是 | The path to a File within Foundry. Paths are relative and must not start with a leading slash.<br>Examples: `my-file.txt`, `path/to/my-file.jpg`, `dataframe.snappy.parquet`.<br>示例: `My Folder/my-file.csv` |
| `File.transactionRid` | string | 是 | The Resource Identifier (RID) of a Transaction.<br>示例: `ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4` |
| `File.sizeBytes` | string | 否 | — |
| `File.updatedTime` | string | 是 | 示例: `2020-09-30T12:34:56.789Z` |

```json
{
  "path": "2020/09/30/trades.csv",
  "updatedTime": "2020-09-30T12:34:56.789Z",
  "transactionRid": "ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `BranchNotFound` | The requested branch could not be found, or the client token does not have access to it. |
| NOT_FOUND | `DatasetNotFound` | The requested dataset could not be found, or the client token does not have access to it. |
| CONFLICT | `OpenTransactionAlreadyExists` | A transaction is already open on this dataset and branch. A branch of a dataset can only have one open transaction at a time. |
| NOT_FOUND | `FileAlreadyExists` | The given file path already exists in the dataset and transaction. |
| INVALID_ARGUMENT | `InvalidParameterCombination` | The given parameters are individually valid but cannot be used in the given combination. |
| INVALID_ARGUMENT | `InvalidFilePath` | The provided file path is invalid. Check that the path does not start with a leading slash. |
| NOT_FOUND | `TransactionNotFound` | The requested transaction could not be found on the dataset, or the client token does not have access to it. |
| INVALID_ARGUMENT | `TransactionNotOpen` | The given transaction is not open. |
| PERMISSION_DENIED | `CreateTransactionPermissionDenied` | The provided token does not have permission to create a transaction on this dataset. |
| PERMISSION_DENIED | `AbortTransactionPermissionDenied` | The provided token does not have permission to abort the given transaction on the given dataset. |
| PERMISSION_DENIED | `CommitTransactionPermissionDenied` | The provided token does not have permission to commit the given transaction on the given dataset. |
| INVALID_ARGUMENT | `InvalidBranchName` | The requested branch name cannot be used. Branch names cannot be empty and must not look like RIDs or UUIDs. |
| PERMISSION_DENIED | `UploadFilePermissionDenied` | Could not upload the File. |
