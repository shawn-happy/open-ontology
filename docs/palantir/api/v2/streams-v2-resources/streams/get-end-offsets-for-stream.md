`GET /api/v2/highScale/streams/datasets/{datasetRid}/streams/{streamBranchName}/getEndOffsets`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Get the end offsets for all partitions of a stream. The end offset is the offset of the next record that will be written to the partition.


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
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**GetEndOffsetsResponse**

The end offsets for each partition of a stream.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GetEndOffsetsResponse` | map | 是 | The end offsets for each partition of a stream.<br>示例: `{"0":100,"1":200}` |
| `GetEndOffsetsResponse.PartitionId` | string | 是 | The identifier for a partition of a Foundry stream. |

```json
{
  "0": 100,
  "1": 200
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `GetEndOffsetsForStreamPermissionDenied` | Could not getEndOffsets the Stream. |
