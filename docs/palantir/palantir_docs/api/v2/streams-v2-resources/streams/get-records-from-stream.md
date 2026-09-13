`GET /api/v2/highScale/streams/datasets/{datasetRid}/streams/{streamBranchName}/getRecords`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Get a batch of records from a stream for a given partition. Offsets are ordered from [0, inf) but may be sparse (e.g.: 0, 2, 3, 5).
Binary field values are returned as base64-encoded strings. Decode them to retrieve the original bytes.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:streams-read`.

**OAuth2 scopes**: `api:streams-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `streamBranchName` | string | 是 | The name of a Branch.<br>示例: `master` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `viewRid` | string | 否 | If provided, this endpoint will only read from the stream corresponding to the specified view RID. If<br>not provided, this endpoint will read from the latest stream on the branch.<br>Providing this value is an advanced configuration, to be used when additional control over the<br>underlying streaming data structures is needed.<br>示例: `ri.foundry-streaming.main.view.ecd4f0f6-8526-4468-9eda-14939449ad79` |
| `partitionId` | string | 是 | The ID of the partition to retrieve records from.<br>示例: `0` |
| `startOffset` | string | 否 | The inclusive beginning of the range to be retrieved. Leave empty when reading from the beginning of the partition. |
| `limit` | integer | 是 | The total number of records to be retrieved. The response may contain fewer records than requested depending on number<br>of records in the partition and server-defined limits.<br>示例: `100` |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**GetRecordsResponse**

A list of records from a stream with their offsets.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GetRecordsResponse` | list<RecordWithOffset> | 是 | A list of records from a stream with their offsets.<br>示例: `[{"offset":42,"value":{"timestamp":1731426022784,"value":"Hello, World!"}}]` |
| `GetRecordsResponse.RecordWithOffset` | object | 是 | A record retrieved from a stream, including its offset within the partition. |
| `GetRecordsResponse.RecordWithOffset.offset` | string | 是 | The offset of the record within the partition. |
| `GetRecordsResponse.RecordWithOffset.value` | map | 否 | The record value as a map of field names to values.<br>示例: `{"timestamp":1731426022784,"value":"Hello, World!"}` |

```json
[
  {
    "offset": 42,
    "value": {
      "timestamp": 1731426022784,
      "value": "Hello, World!"
    }
  }
]
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `GetRecordsFromStreamPermissionDenied` | Could not getRecords the Stream. |
