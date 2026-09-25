`GET /api/v2/sds/scanConfigurations/{scanConfigurationId}/records`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Retrieve a page of scan records for a specific ScanConfigurationId.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:sds-records-read`.

**OAuth2 scopes**: `api:sds-records-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `scanConfigurationId` | string | 是 | Identifier of a ScanConfiguration. Can refer to a recurring scan or a one-time scan.<br>示例: `ri.foundry-inference.main.auto-inference-config.7f99a5a0-7692-420f-a79c-53756c3e9259` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `scopeRid` | string | 是 | The space containing the scanned resources.<br>示例: `ri.compass.main.folder.833bc64a-f8fb-4ade-834e-6814242613ae` |
| `scanRecordIsActiveFilter` | enum | 否 | If no value is provided, active as well as completed scans will be returned.<br>示例: `ALL_SCANS` |
| `scanResultStatusFilter` | enum | 否 | If no value is provided, all scan records regardless of ScanResultStatus will be returned.<br>示例: `SUCCEEDED_WITH_MATCHES` |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**ListScanRecordsResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListScanRecordsResponse` | object | 是 | 示例: `{"data":[{"branchId":"master","matchActionRids":["ri.foundry-inference.main.inference-action.a3cff674-6303-4900-a0f6-3d3ea44bcdcc"],"completedTime":"2026-03-16T18:51:04.218Z","matchConditionRids":["ri.foundry-inference.main.match-condition.c12f6d47-197a-3f9a-ba1e-e1331133fa9e"],"startTime":"2026-03-16T18:51:00.527Z","id":"a5621a18-0412-4304-8eb8-22de9bbeeffe","scanResult":[{"matchConditionRid":"ri.foundry-inference.main.match-condition.c12f6d47-197a-3f9a-ba1e-e1331133fa9e","numberOfScannedRows":15000,"result":{"type":"datasetMatch","matchedColumns":[{"columnName":"email_address","numberOfMatchedRows":120},{"columnName":"phone_number","numberOfMatchedRows":45}]}},{"matchConditionRid":"ri.foundry-inference.main.match-condition.a1b2c3d4-5678-9abc-def0-1234567890ab","numberOfScannedRows":15000,"result":{"type":"noMatch"}}],"scannedResource":"ri.foundry.main.dataset.437313ac-11dc-4e23-8c4c-3d510f1b10be","scanConfigurationId":"ri.foundry-inference.main.auto-inference-config.7f99a5a0-7692-420f-a79c-53756c3e9259","status":"SUCCEEDED_WITH_MATCHES"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListScanRecordsResponse.data` | list<ScanRecord> | 否 | — |
| `ListScanRecordsResponse.data.ScanRecord` | object | 是 | — |
| `ListScanRecordsResponse.data.ScanRecord.id` | string | 是 | Identifier of a ScanRecord.<br>示例: `a5621a18-0412-4304-8eb8-22de9bbeeffe` |
| `ListScanRecordsResponse.data.ScanRecord.scanConfigurationId` | string | 是 | Identifier of a ScanConfiguration. Can refer to a recurring scan or a one-time scan.<br>示例: `ri.foundry-inference.main.auto-inference-config.7f99a5a0-7692-420f-a79c-53756c3e9259` |
| `ListScanRecordsResponse.data.ScanRecord.scannedResource` | string | 是 | The scanned resource.<br>示例: `ri.foundry.main.dataset.437313ac-11dc-4e23-8c4c-3d510f1b10be` |
| `ListScanRecordsResponse.data.ScanRecord.branchId` | string | 是 | The scanned branch.<br>示例: `master` |
| `ListScanRecordsResponse.data.ScanRecord.transactionRid` | string | 否 | — |
| `ListScanRecordsResponse.data.ScanRecord.startTime` | string | 是 | The start timestamp of the scan.<br>示例: `2026-03-16T18:51:00.527Z` |
| `ListScanRecordsResponse.data.ScanRecord.completedTime` | string | 否 | The completion timestamp of the scan.<br>示例: `2026-03-16T18:51:04.218Z` |
| `ListScanRecordsResponse.data.ScanRecord.matchConditionRids` | list<MatchConditionRid> | 否 | — |
| `ListScanRecordsResponse.data.ScanRecord.matchConditionRids.MatchConditionRid` | string | 是 | Resource Identifier of a MatchCondition. |
| `ListScanRecordsResponse.data.ScanRecord.matchActionRids` | list<MatchActionRid> | 否 | — |
| `ListScanRecordsResponse.data.ScanRecord.matchActionRids.MatchActionRid` | string | 是 | Resource Identifier of a MatchAction. |
| `ListScanRecordsResponse.data.ScanRecord.buildRid` | string | 否 | — |
| `ListScanRecordsResponse.data.ScanRecord.status` | enum | 是 | The result status of a sensitive data scan.<br>示例: `SUCCEEDED_WITH_MATCHES` |
| `ListScanRecordsResponse.data.ScanRecord.scanResult` | list<MatchConditionOutcome> | 否 | 示例: `[{"matchConditionRid":"ri.foundry-inference.main.match-condition.c12f6d47-197a-3f9a-ba1e-e1331133fa9e","numberOfScannedRows":15000,"result":{"type":"datasetMatch","matchedColumns":[{"columnName":"email_address","numberOfMatchedRows":120},{"columnName":"phone_number","numberOfMatchedRows":45}]}},{"matchConditionRid":"ri.foundry-inference.main.match-condition.a1b2c3d4-5678-9abc-def0-1234567890ab","numberOfScannedRows":15000,"result":{"type":"noMatch"}}]` |
| `ListScanRecordsResponse.data.ScanRecord.scanResult.MatchConditionOutcome` | object | 是 | Represents the outcome of the evaluation of a specific MatchCondition. Includes the number of scanned rows and the scan result. |
| `ListScanRecordsResponse.data.ScanRecord.scanResult.MatchConditionOutcome.matchConditionRid` | string | 是 | Resource Identifier of a MatchCondition.<br>示例: `ri.foundry-inference.main.match-condition.c12f6d47-197a-3f9a-ba1e-e1331133fa9e` |
| `ListScanRecordsResponse.data.ScanRecord.scanResult.MatchConditionOutcome.numberOfScannedRows` | string | 是 | — |
| `ListScanRecordsResponse.data.ScanRecord.scanResult.MatchConditionOutcome.result` | union | 是 | Represents the scan result for a specific MatchCondition. |
| `ListScanRecordsResponse.data.ScanRecord.scanResult.MatchConditionOutcome.result.noMatch` | object | 否 | Indicates that the scanned MatchCondition was not detected in the scanned data. |
| `ListScanRecordsResponse.data.ScanRecord.scanResult.MatchConditionOutcome.result.datasetMatch` | object | 否 | Indicates that matches were found for the scanned MatchCondition. |
| `ListScanRecordsResponse.data.ScanRecord.scanResult.MatchConditionOutcome.result.datasetMatch.matchedColumns` | list<ColumnMatch> | 否 | — |
| `ListScanRecordsResponse.data.ScanRecord.scanResult.MatchConditionOutcome.result.datasetMatch.matchedColumns.ColumnMatch` | object | 是 | Represents a column that contains rows that matched a scanned MatchCondition. |
| `ListScanRecordsResponse.data.ScanRecord.scanResult.MatchConditionOutcome.result.datasetMatch.matchedColumns.ColumnMatch.columnName` | string | 是 | — |
| `ListScanRecordsResponse.data.ScanRecord.scanResult.MatchConditionOutcome.result.datasetMatch.matchedColumns.ColumnMatch.numberOfMatchedRows` | string | 是 | — |
| `ListScanRecordsResponse.data.ScanRecord.scanResult.MatchConditionOutcome.result.mediasetMatch` | object | 否 | Represents a media set that contains media items that matched a scanned MatchCondition. |
| `ListScanRecordsResponse.data.ScanRecord.scanResult.MatchConditionOutcome.result.mediasetMatch.numberOfMatchedMediaItems` | string | 是 | — |
| `ListScanRecordsResponse.data.ScanRecord.scanResult.MatchConditionOutcome.result.mediasetMatch.examples` | list<MediaMatchExample> | 否 | — |
| `ListScanRecordsResponse.data.ScanRecord.scanResult.MatchConditionOutcome.result.mediasetMatch.examples.MediaMatchExample` | union | 是 | Represents an example of a media item that matched a scanned MatchCondition. |
| `ListScanRecordsResponse.data.ScanRecord.scanResult.MatchConditionOutcome.result.mediasetMatch.examples.MediaMatchExample.mediaItemExample` | object | 否 | Represents an example of a media item that matched a scanned MatchCondition. |
| `ListScanRecordsResponse.data.ScanRecord.scanResult.MatchConditionOutcome.result.mediasetMatch.examples.MediaMatchExample.mediaItemExample.mediaItemRid` | string | 是 | — |
| `ListScanRecordsResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "branchId": "master",
      "matchActionRids": [
        "ri.foundry-inference.main.inference-action.a3cff674-6303-4900-a0f6-3d3ea44bcdcc"
      ],
      "completedTime": "2026-03-16T18:51:04.218Z",
      "matchConditionRids": [
        "ri.foundry-inference.main.match-condition.c12f6d47-197a-3f9a-ba1e-e1331133fa9e"
      ],
      "startTime": "2026-03-16T18:51:00.527Z",
      "id": "a5621a18-0412-4304-8eb8-22de9bbeeffe",
      "scanResult": [
        {
          "matchConditionRid": "ri.foundry-inference.main.match-condition.c12f6d47-197a-3f9a-ba1e-e1331133fa9e",
          "numberOfScannedRows": 15000,
          "result": {
            "type": "datasetMatch",
            "matchedColumns": [
              {
                "columnName": "email_address",
                "numberOfMatchedRows": 120
              },
              {
                "columnName": "phone_number",
                "numberOfMatchedRows": 45
              }
            ]
          }
        },
        {
          "matchConditionRid": "ri.foundry-inference.main.match-condition.a1b2c3d4-5678-9abc-def0-1234567890ab",
          "numberOfScannedRows": 15000,
          "result": {
            "type": "noMatch"
          }
        }
      ],
      "scannedResource": "ri.foundry.main.dataset.437313ac-11dc-4e23-8c4c-3d510f1b10be",
      "scanConfigurationId": "ri.foundry-inference.main.auto-inference-config.7f99a5a0-7692-420f-a79c-53756c3e9259",
      "status": "SUCCEEDED_WITH_MATCHES"
    }
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `ScanConfigurationNotFound` | The ScanConfiguration could not be found. |
