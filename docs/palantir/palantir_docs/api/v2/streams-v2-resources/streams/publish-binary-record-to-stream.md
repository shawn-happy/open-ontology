`POST /api/v2/highScale/streams/datasets/{datasetRid}/streams/{streamBranchName}/publishBinaryRecord`

Publish a single binary record to the stream. The stream's schema must be a single binary field.


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
| `viewRid` | string | 否 | If provided, this endpoint will only write to the stream corresponding to the specified view RID. If<br>not provided, this endpoint will write to the latest stream on the branch.<br>Providing this value is an advanced configuration, to be used when additional control over the<br>underlying streaming data structures is needed.<br>示例: `ri.foundry-streaming.main.view.ecd4f0f6-8526-4468-9eda-14939449ad79` |

## Request body

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `PublishBinaryRecordToStreamPermissionDenied` | Could not publishBinaryRecord the Stream. |
