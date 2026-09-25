`POST /api/v2/mediasets/{mediaSetRid}/items/register`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Registers a media item that currently resides in a federated media store. Registration will validate the item
against the media set's schema and perform initial metadata extraction.
This endpoint is only applicable for federated media sets.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:mediasets-write`.

**OAuth2 scopes**: `api:mediasets-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mediaSetRid` | string | 是 | The Resource Identifier (RID) of a Media Set in Foundry. |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `branchName` | string | 否 | Specifies the specific branch by name to which this media item will be registered. |
| `viewRid` | string | 否 | Specifies the specific view by rid to which this media item will be registered. |
| `transactionId` | string | 否 | The id of the transaction associated with this request. Required for transactional media sets. |
| `preview` | boolean | 否 | A boolean flag that, when set to true, enables the use of beta features in preview mode.<br>示例: `true` |

## Request body

## Response

**RegisterMediaItemResponse**

Response after successfully registering a media item.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `RegisterMediaItemResponse` | object | 是 | Response after successfully registering a media item. |
| `RegisterMediaItemResponse.mediaItemRid` | string | 是 | The Resource Identifier (RID) of an individual Media Item within a Media Set in Foundry. |
| `RegisterMediaItemResponse.mediaType` | string | 是 | The [media type](https://www.iana.org/assignments/media-types/media-types.xhtml) of the file or attachment.<br>Examples: `application/json`, `application/pdf`, `application/octet-stream`, `image/jpeg`<br>示例: `application/pdf` |
