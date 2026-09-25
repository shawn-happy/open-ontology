`GET /api/v2/datasets/{datasetRid}/getSchema`

Gets a dataset's schema. If no `endTransactionRid` is provided, the latest committed version will be used.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:datasets-read`.

**OAuth2 scopes**: `api:datasets-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `branchName` | string | 否 | The name of a Branch.<br>示例: `master` |
| `endTransactionRid` | string | 否 | The Resource Identifier (RID) of the end Transaction. If a user does not provide a value, the RID of the latest committed transaction will be used.<br>示例: `ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4` |
| `versionId` | string | 否 | The schema version that should be used. If none is provided, the latest version will be used.<br>示例: `0000000d-2acf-537c-a228-3a9fe3cdc523` |

## Response

**GetDatasetSchemaResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GetDatasetSchemaResponse` | object | 是 | 示例: `{"endTransactionRid":"ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4","schema":{"fieldSchemaList":[{"name":"id","type":"LONG","nullable":false,"customMetadata":{"description":"Primary key"}},{"name":"event_time","type":"TIMESTAMP","nullable":false},{"name":"price","type":"DECIMAL","precision":10,"scale":2,"nullable":true},{"name":"tags","type":"ARRAY","nullable":true,"arraySubtype":{"type":"STRING","nullable":false}},{"name":"metrics","type":"STRUCT","nullable":true,"subSchemas":[{"name":"temperature","type":"DOUBLE","nullable":true},{"name":"humidity","type":"DOUBLE","nullable":true}]}]},"versionId":"0000000d-2acf-537c-a228-3a9fe3cdc523","branchName":"master"}` |
| `GetDatasetSchemaResponse.branchName` | string | 是 | The name of a Branch.<br>示例: `master` |
| `GetDatasetSchemaResponse.endTransactionRid` | string | 是 | The Resource Identifier (RID) of a Transaction.<br>示例: `ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4` |
| `GetDatasetSchemaResponse.schema` | object | 是 | The schema for a Foundry dataset. Files uploaded to this dataset must match this schema.<br>示例: `{"fieldSchemaList":[{"name":"id","type":"LONG","nullable":false,"customMetadata":{"description":"Primary key"}},{"name":"event_time","type":"TIMESTAMP","nullable":false},{"name":"price","type":"DECIMAL","precision":10,"scale":2,"nullable":true},{"name":"tags","type":"ARRAY","nullable":true,"arraySubtype":{"type":"STRING","nullable":false}},{"name":"metrics","type":"STRUCT","nullable":true,"subSchemas":[{"name":"temperature","type":"DOUBLE","nullable":true},{"name":"humidity","type":"DOUBLE","nullable":true}]}]}` |
| `GetDatasetSchemaResponse.schema.fieldSchemaList` | list<DatasetFieldSchema> | 否 | — |
| `GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema` | object | 是 | A field in a Foundry dataset. |
| `GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.type` | enum | 是 | The data type of a column in a dataset schema.<br>示例: `ARRAY` |
| `GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.name` | string | 否 | The name of a column. May be absent in nested schema objects. |
| `GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.nullable` | boolean | 是 | Indicates whether values of this field may be null. |
| `GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.userDefinedTypeClass` | string | 否 | Canonical classname of the user-defined type for this field. This should be a subclass of Spark's `UserDefinedType`. |
| `GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.customMetadata` | map | 否 | User-supplied custom metadata about the column, such as Foundry web archetypes, descriptions, etc. |
| `GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.arraySubtype` | object | 否 | Only used when field type is array.<br>示例: `{"type":"ARRAY"}` |
| `GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.precision` | integer | 否 | Only used when field type is decimal. |
| `GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.scale` | integer | 否 | Only used when field type is decimal. |
| `GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.mapKeyType` | object | 否 | Only used when field type is map.<br>示例: `{"type":"ARRAY"}` |
| `GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.mapValueType` | object | 否 | Only used when field type is map.<br>示例: `{"type":"ARRAY"}` |
| `GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.subSchemas` | list<DatasetFieldSchema> | 否 | Only used when field type is struct. |
| `GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.subSchemas.DatasetFieldSchema` | object | 是 | A field in a Foundry dataset. |
| `GetDatasetSchemaResponse.versionId` | string | 是 | The version identifier of a dataset schema.<br>示例: `0000000d-2acf-537c-a228-3a9fe3cdc523` |

```json
{
  "endTransactionRid": "ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4",
  "schema": {
    "fieldSchemaList": [
      {
        "name": "id",
        "type": "LONG",
        "nullable": false,
        "customMetadata": {
          "description": "Primary key"
        }
      },
      {
        "name": "event_time",
        "type": "TIMESTAMP",
        "nullable": false
      },
      {
        "name": "price",
        "type": "DECIMAL",
        "precision": 10,
        "scale": 2,
        "nullable": true
      },
      {
        "name": "tags",
        "type": "ARRAY",
        "nullable": true,
        "arraySubtype": {
          "type": "STRING",
          "nullable": false
        }
      },
      {
        "name": "metrics",
        "type": "STRUCT",
        "nullable": true,
        "subSchemas": [
          {
            "name": "temperature",
            "type": "DOUBLE",
            "nullable": true
          },
          {
            "name": "humidity",
            "type": "DOUBLE",
            "nullable": true
          }
        ]
      }
    ]
  },
  "versionId": "0000000d-2acf-537c-a228-3a9fe3cdc523",
  "branchName": "master"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `BranchNotFound` | The requested branch could not be found, or the client token does not have access to it. |
| NOT_FOUND | `DatasetNotFound` | The requested dataset could not be found, or the client token does not have access to it. |
| NOT_FOUND | `SchemaNotFound` | A schema could not be found for the given dataset and branch, or the client token does not have access to it. |
| INVALID_ARGUMENT | `InvalidParameterCombination` | The given parameters are individually valid but cannot be used in the given combination. |
| NOT_FOUND | `DatasetViewNotFound` | The requested dataset view could not be found. A dataset view represents the effective file contents of a dataset<br>for a branch at a point in time, calculated from transactions (SNAPSHOT, APPEND, UPDATE, DELETE). The view may not<br>exist if the dataset has no transactions, contains no files, the branch is not valid, or the client token does not have access to it. |
| PERMISSION_DENIED | `GetDatasetSchemaPermissionDenied` | Could not getSchema the Dataset. |
