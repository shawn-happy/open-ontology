`GET /api/v2/mediasets/{mediaSetRid}/items/getRidByPath`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Returns the media item RID for the media item with the specified path.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:mediasets-read`.

**OAuth2 scopes**: `api:mediasets-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mediaSetRid` | string | 是 | The RID of the media set. |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mediaItemPath` | string | 是 | The path of the media item. |
| `branchName` | string | 否 | Specifies the specific branch by name in which to search for this media item. May not be provided if branch rid or view rid are provided. |
| `branchRid` | string | 否 | Specifies the specific branch by rid in which to search for this media item. May not be provided if branch name or view rid are provided. |
| `viewRid` | string | 否 | Specifies the specific view by rid in which to search for this media item. May not be provided if branch name or branch rid are provided. |
| `preview` | boolean | 否 | A boolean flag that, when set to true, enables the use of beta features in preview mode.<br>示例: `true` |

## Response

**GetMediaItemRidByPathResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GetMediaItemRidByPathResponse` | object | 是 | — |
| `GetMediaItemRidByPathResponse.mediaItemRid` | string | 否 | The Resource Identifier (RID) of an individual Media Item within a Media Set in Foundry. |
