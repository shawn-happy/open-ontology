`POST /api/v2/highScale/streams/datasets/{datasetRid}/streams/{streamBranchName}/publishRecords`

Publish a batch of records to the stream. The records will be validated against the stream's schema, and
the batch will be rejected if one or more of the records are invalid.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:streams-write`.

**OAuth2 scopes**: `api:streams-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `streamBranchName` | string | 是 | The name of a Branch.<br>示例: `master` |

## Request body

```json
{
  "records": [
    {
      "timestamp": 1731426022784,
      "value": "Hello, World!"
    }
  ],
  "viewRid": "ri.foundry-streaming.main.view.ecd4f0f6-8526-4468-9eda-14939449ad79"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `PublishRecordsToStreamPermissionDenied` | Could not publishRecords the Stream. |
