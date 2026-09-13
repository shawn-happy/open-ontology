`GET /api/v2/mediasets/{mediaSetRid}`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Gets information about the media set.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:mediasets-read`.

**OAuth2 scopes**: `api:mediasets-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mediaSetRid` | string | 是 | The Resource Identifier (RID) of a Media Set in Foundry. |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | A boolean flag that, when set to true, enables the use of beta features in preview mode.<br>示例: `true` |

## Response

**GetMediaSetResponse**

Information about a media set.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GetMediaSetResponse` | object | 是 | Information about a media set. |
| `GetMediaSetResponse.rid` | string | 是 | The Resource Identifier (RID) of a Media Set in Foundry. |
| `GetMediaSetResponse.mediaSchema` | enum | 是 | The schema type of a media set, indicating what type of media items it can contain. |
| `GetMediaSetResponse.defaultBranchName` | string | 是 | A name for a media set branch. Valid branch names must be (a) non-empty, (b) less than 256 characters, and<br>(c) not a valid ResourceIdentifier. |
| `GetMediaSetResponse.transactionPolicy` | union | 是 | The transaction policy for a media set, determining how writes are handled. |
| `GetMediaSetResponse.transactionPolicy.batchTransactions` | object | 否 | All writes must be part of a transaction. Transactions are branch-scoped and created by calling<br>create transaction. Writes are not visible until commit transaction is called. |
| `GetMediaSetResponse.transactionPolicy.noTransactions` | object | 否 | Writes are not part of a transaction and are immediately visible.<br>Calls to create transaction or commit transaction will error. |
| `GetMediaSetResponse.pathsRequired` | boolean | 是 | Whether media items in this media set require paths. |
