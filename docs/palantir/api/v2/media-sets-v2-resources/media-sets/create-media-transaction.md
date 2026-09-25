`POST /api/v2/mediasets/{mediaSetRid}/transactions`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Creates a new transaction. Items uploaded to the media set while this transaction is open will not be reflected until the transaction is committed.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:mediasets-write`.

**OAuth2 scopes**: `api:mediasets-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mediaSetRid` | string | 是 | The Resource Identifier (RID) of a Media Set in Foundry. |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `branchName` | string | 否 | The branch on which to open the transaction. Defaults to `master` for most enrollments. |
| `preview` | boolean | 否 | A boolean flag that, when set to true, enables the use of beta features in preview mode.<br>示例: `true` |

## Response

**TransactionId**

An identifier which represents a transaction on a media set.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `TransactionId` | string | 是 | An identifier which represents a transaction on a media set.<br>示例: `{"transactionId":"550e8400-e29b-41d4-a716-446655440000"}` |

```json
{
  "transactionId": "550e8400-e29b-41d4-a716-446655440000"
}
```
