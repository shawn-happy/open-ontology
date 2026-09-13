`POST /api/v2/datasets/getSchemaBatch`

Fetch schemas for multiple datasets in a single request. Datasets not found 
or inaccessible to the user will be omitted from the response.


The maximum batch size for this endpoint is 1000.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:datasets-read`.

**OAuth2 scopes**: `api:datasets-read`

## Request body

```json
[
  {
    "endTransactionRid": "ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4",
    "datasetRid": "ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da",
    "versionId": "0000000d-2acf-537c-a228-3a9fe3cdc523",
    "branchName": "master"
  }
]
```

## Response

**GetSchemaDatasetsBatchResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GetSchemaDatasetsBatchResponse` | object | 是 | 示例: `{"data":{"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da":{"endTransactionRid":"ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4","schema":{"fieldSchemaList":[{"name":"id","type":"LONG","nullable":false,"customMetadata":{"description":"Primary key"}},{"name":"event_time","type":"TIMESTAMP","nullable":false},{"name":"price","type":"DECIMAL","precision":10,"scale":2,"nullable":true},{"name":"tags","type":"ARRAY","nullable":true,"arraySubtype":{"type":"STRING","nullable":false}},{"name":"metrics","type":"STRUCT","nullable":true,"subSchemas":[{"name":"temperature","type":"DOUBLE","nullable":true},{"name":"humidity","type":"DOUBLE","nullable":true}]}]},"versionId":"0000000d-2acf-537c-a228-3a9fe3cdc523","branchName":"master"}}}` |
| `GetSchemaDatasetsBatchResponse.data` | map | 否 | — |
| `GetSchemaDatasetsBatchResponse.data.DatasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset. |
| `GetSchemaDatasetsBatchResponse.data.GetDatasetSchemaResponse` | object | 是 | — |
| `GetSchemaDatasetsBatchResponse.data.GetDatasetSchemaResponse.branchName` | string | 是 | The name of a Branch.<br>示例: `master` |
| `GetSchemaDatasetsBatchResponse.data.GetDatasetSchemaResponse.endTransactionRid` | string | 是 | The Resource Identifier (RID) of a Transaction.<br>示例: `ri.foundry.main.transaction.0a0207cb-26b7-415b-bc80-66a3aa3933f4` |
| `GetSchemaDatasetsBatchResponse.data.GetDatasetSchemaResponse.schema` | object | 是 | The schema for a Foundry dataset. Files uploaded to this dataset must match this schema.<br>示例: `{"fieldSchemaList":[{"name":"id","type":"LONG","nullable":false,"customMetadata":{"description":"Primary key"}},{"name":"event_time","type":"TIMESTAMP","nullable":false},{"name":"price","type":"DECIMAL","precision":10,"scale":2,"nullable":true},{"name":"tags","type":"ARRAY","nullable":true,"arraySubtype":{"type":"STRING","nullable":false}},{"name":"metrics","type":"STRUCT","nullable":true,"subSchemas":[{"name":"temperature","type":"DOUBLE","nullable":true},{"name":"humidity","type":"DOUBLE","nullable":true}]}]}` |
| `GetSchemaDatasetsBatchResponse.data.GetDatasetSchemaResponse.schema.fieldSchemaList` | list<DatasetFieldSchema> | 否 | — |
| `GetSchemaDatasetsBatchResponse.data.GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema` | object | 是 | A field in a Foundry dataset. |
| `GetSchemaDatasetsBatchResponse.data.GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.type` | enum | 是 | The data type of a column in a dataset schema.<br>示例: `ARRAY` |
| `GetSchemaDatasetsBatchResponse.data.GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.name` | string | 否 | The name of a column. May be absent in nested schema objects. |
| `GetSchemaDatasetsBatchResponse.data.GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.nullable` | boolean | 是 | Indicates whether values of this field may be null. |
| `GetSchemaDatasetsBatchResponse.data.GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.userDefinedTypeClass` | string | 否 | Canonical classname of the user-defined type for this field. This should be a subclass of Spark's `UserDefinedType`. |
| `GetSchemaDatasetsBatchResponse.data.GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.customMetadata` | map | 否 | User-supplied custom metadata about the column, such as Foundry web archetypes, descriptions, etc. |
| `GetSchemaDatasetsBatchResponse.data.GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.arraySubtype` | object | 否 | Only used when field type is array.<br>示例: `{"type":"ARRAY"}` |
| `GetSchemaDatasetsBatchResponse.data.GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.precision` | integer | 否 | Only used when field type is decimal. |
| `GetSchemaDatasetsBatchResponse.data.GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.scale` | integer | 否 | Only used when field type is decimal. |
| `GetSchemaDatasetsBatchResponse.data.GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.mapKeyType` | object | 否 | Only used when field type is map.<br>示例: `{"type":"ARRAY"}` |
| `GetSchemaDatasetsBatchResponse.data.GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.mapValueType` | object | 否 | Only used when field type is map.<br>示例: `{"type":"ARRAY"}` |
| `GetSchemaDatasetsBatchResponse.data.GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.subSchemas` | list<DatasetFieldSchema> | 否 | Only used when field type is struct. |
| `GetSchemaDatasetsBatchResponse.data.GetDatasetSchemaResponse.schema.fieldSchemaList.DatasetFieldSchema.subSchemas.DatasetFieldSchema` | object | 是 | A field in a Foundry dataset. |
| `GetSchemaDatasetsBatchResponse.data.GetDatasetSchemaResponse.versionId` | string | 是 | The version identifier of a dataset schema.<br>示例: `0000000d-2acf-537c-a228-3a9fe3cdc523` |

```json
{
  "data": {
    "ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da": {
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
  }
}
```
