`POST /api/v2/streams/datasets/{datasetRid}/streams/{streamBranchName}/reset`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Reset the stream on the given dataset branch, clearing the existing records and allowing new configurations
to be applied.

To change the stream settings without clearing the records, update the stream settings in-platform.

This will create a new stream view (as seen by the change of the `viewRid` on the branch),
which will be the new stream view that will be written to for the branch.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:streams-write`.

**OAuth2 scopes**: `api:streams-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `streamBranchName` | string | 是 | The name of a Branch.<br>示例: `master` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
{
  "schema": {
    "fields": [
      {
        "name": "timestamp",
        "schema": {
          "nullable": false,
          "dataType": {
            "type": "timestamp"
          }
        }
      },
      {
        "name": "value",
        "schema": {
          "nullable": false,
          "dataType": {
            "type": "string"
          }
        }
      }
    ],
    "keyFieldNames": [
      "timestamp"
    ]
  },
  "partitionsCount": 1,
  "streamType": "LOW_LATENCY",
  "compressed": false
}
```

## Response

**Stream**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Stream` | object | 是 | 示例: `{"schema":{"fields":[{"name":"timestamp","schema":{"nullable":false,"dataType":{"type":"timestamp"}}},{"name":"value","schema":{"nullable":false,"dataType":{"type":"string"}}}],"keyFieldNames":["timestamp"]},"partitionsCount":1,"streamType":"LOW_LATENCY","branchName":"master","viewRid":"ri.foundry-streaming.main.view.ecd4f0f6-8526-4468-9eda-14939449ad79","compressed":false}` |
| `Stream.branchName` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Stream.schema` | object | 是 | The Foundry schema for this stream.<br>示例: `{"fields":[{"name":"timestamp","schema":{"nullable":false,"dataType":{"type":"timestamp"}}},{"name":"value","schema":{"nullable":false,"dataType":{"type":"string"}}}],"keyFieldNames":["timestamp"]}` |
| `Stream.schema.fields` | list<Field> | 否 | — |
| `Stream.schema.fields.Field` | object | 是 | A field in a Foundry schema. For more information on supported data types, see the<br>[supported field types](/docs/foundry/data-integration/datasets/#supported-field-types) user documentation. |
| `Stream.schema.fields.Field.name` | string | 是 | — |
| `Stream.schema.fields.Field.schema` | object | 是 | The specification of the type of a Foundry schema field. |
| `Stream.schema.fields.Field.schema.nullable` | boolean | 是 | — |
| `Stream.schema.fields.Field.schema.customMetadata` | map | 否 | — |
| `Stream.schema.fields.Field.schema.dataType` | union | 是 | — |
| `Stream.schema.fields.Field.schema.dataType.struct` | object | 否 | — |
| `Stream.schema.fields.Field.schema.dataType.struct.subFields` | list<Field> | 否 | — |
| `Stream.schema.fields.Field.schema.dataType.struct.subFields.Field` | object | 是 | A field in a Foundry schema. For more information on supported data types, see the<br>[supported field types](/docs/foundry/data-integration/datasets/#supported-field-types) user documentation. |
| `Stream.schema.fields.Field.schema.dataType.date` | object | 否 | — |
| `Stream.schema.fields.Field.schema.dataType.string` | object | 否 | — |
| `Stream.schema.fields.Field.schema.dataType.byte` | object | 否 | — |
| `Stream.schema.fields.Field.schema.dataType.double` | object | 否 | — |
| `Stream.schema.fields.Field.schema.dataType.integer` | object | 否 | — |
| `Stream.schema.fields.Field.schema.dataType.float` | object | 否 | — |
| `Stream.schema.fields.Field.schema.dataType.long` | object | 否 | — |
| `Stream.schema.fields.Field.schema.dataType.boolean` | object | 否 | — |
| `Stream.schema.fields.Field.schema.dataType.array` | object | 否 | — |
| `Stream.schema.fields.Field.schema.dataType.array.itemsSchema` | object | 是 | The specification of the type of a Foundry schema field. |
| `Stream.schema.fields.Field.schema.dataType.binary` | object | 否 | — |
| `Stream.schema.fields.Field.schema.dataType.short` | object | 否 | — |
| `Stream.schema.fields.Field.schema.dataType.decimal` | object | 否 | — |
| `Stream.schema.fields.Field.schema.dataType.decimal.precision` | integer | 否 | The total number of digits of the Decimal type. The maximum value is 38.<br>示例: `38` |
| `Stream.schema.fields.Field.schema.dataType.decimal.scale` | integer | 否 | The number of digits to the right of the decimal point. The maximum value is 38.<br>示例: `18` |
| `Stream.schema.fields.Field.schema.dataType.map` | object | 否 | — |
| `Stream.schema.fields.Field.schema.dataType.map.keySchema` | object | 是 | The specification of the type of a Foundry schema field. |
| `Stream.schema.fields.Field.schema.dataType.map.valueSchema` | object | 是 | The specification of the type of a Foundry schema field. |
| `Stream.schema.fields.Field.schema.dataType.timestamp` | object | 否 | — |
| `Stream.schema.keyFieldNames` | list<FieldName> | 否 | The names of the fields to be used as keys for partitioning records. These key fields are used to group<br>all records with the same key into the same partition, to guarantee processing order of grouped records. These<br>keys are not meant to uniquely identify records, and do not by themselves deduplicate records. To deduplicate<br>records, provide a change data capture configuration for the schema.<br>Key fields can only be of the following types:<br>- Boolean<br>- Byte<br>- Date<br>- Decimal<br>- Integer<br>- Long<br>- Short<br>- String<br>- Timestamp<br>For additional information on keys for Foundry streams, see the<br>[streaming keys](/docs/foundry/building-pipelines/streaming-keys/) user documentation. |
| `Stream.schema.keyFieldNames.FieldName` | string | 是 | — |
| `Stream.schema.changeDataCapture` | union | 否 | Configuration for utilizing the stream as a change data capture (CDC) dataset. To configure CDC on a stream, at<br>least one key needs to be provided.<br>For more information on CDC in<br>Foundry, see the [Change Data Capture](/docs/foundry/data-integration/change-data-capture/) user documentation. |
| `Stream.schema.changeDataCapture.fullRow` | object | 否 | Configuration for change data capture which resolves the latest state of the dataset based on new full rows<br>being pushed to the stream. For example, if a value for a row is updated, it is only sufficient to publish<br>the entire new state of that row to the stream. |
| `Stream.schema.changeDataCapture.fullRow.deletionFieldName` | string | 是 | The name of a boolean field in the schema that indicates whether or not a row has been deleted. |
| `Stream.schema.changeDataCapture.fullRow.orderingFieldName` | string | 是 | The name of an ordering field that determines the newest state for a row in the dataset.<br>The ordering field can only be of the following types:<br>- Byte<br>- Date<br>- Decimal<br>- Integer<br>- Long<br>- Short<br>- String<br>- Timestamp |
| `Stream.viewRid` | string | 是 | The view that this stream corresponds to.<br>示例: `ri.foundry-streaming.main.view.ecd4f0f6-8526-4468-9eda-14939449ad79` |
| `Stream.partitionsCount` | integer | 是 | The number of partitions for the Foundry stream. Defaults to 1.<br>Generally, each partition can handle about 5 mb/s of data, so for higher volume streams, more partitions<br>are recommended.<br>示例: `1` |
| `Stream.streamType` | enum | 是 | A conceptual representation of the expected shape of the data for a stream. HIGH_THROUGHPUT and<br>LOW_LATENCY are not compatible with each other. Defaults to LOW_LATENCY.<br>示例: `LOW_LATENCY` |
| `Stream.compressed` | boolean | 是 | Whether or not compression is enabled for the stream. Defaults to false.<br>示例: `false` |

```json
{
  "schema": {
    "fields": [
      {
        "name": "timestamp",
        "schema": {
          "nullable": false,
          "dataType": {
            "type": "timestamp"
          }
        }
      },
      {
        "name": "value",
        "schema": {
          "nullable": false,
          "dataType": {
            "type": "string"
          }
        }
      }
    ],
    "keyFieldNames": [
      "timestamp"
    ]
  },
  "partitionsCount": 1,
  "streamType": "LOW_LATENCY",
  "branchName": "master",
  "viewRid": "ri.foundry-streaming.main.view.ecd4f0f6-8526-4468-9eda-14939449ad79",
  "compressed": false
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `InvalidSchema` | The schema failed validations |
| INVALID_ARGUMENT | `InvalidFieldSchema` | The field schema failed validations |
| INVALID_ARGUMENT | `InvalidStreamNoSchema` | The requested stream exists but is invalid, as it does not have a schema. |
| INVALID_ARGUMENT | `InvalidStreamType` | The stream type is invalid. |
| PERMISSION_DENIED | `ResetStreamPermissionDenied` | Could not reset the Stream. |
| NOT_FOUND | `StreamNotFound` | The given Stream could not be found. |
