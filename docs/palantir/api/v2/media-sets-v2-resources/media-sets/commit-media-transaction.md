`POST /api/v2/mediasets/{mediaSetRid}/transactions/{transactionId}/commit`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Commits an open transaction. On success, items uploaded to the media set during this transaction will become available.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:mediasets-write`.

**OAuth2 scopes**: `api:mediasets-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mediaSetRid` | string | 是 | The Resource Identifier (RID) of a Media Set in Foundry. |
| `transactionId` | string | 是 | An identifier which represents a transaction on a media set. |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | A boolean flag that, when set to true, enables the use of beta features in preview mode.<br>示例: `true` |
