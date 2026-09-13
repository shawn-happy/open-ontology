`GET /api/v2/ontologies/attachments/{attachmentRid}/content`

Get the content of an attachment.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-read`.

**OAuth2 scopes**: `api:ontologies-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `attachmentRid` | string | 是 | The RID of the attachment.<br>示例: `ri.attachments.main.attachment.bb32154e-e043-4b00-9461-93136ca96b6f` |

## Response

**body**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `body` | string | 是 | Success response. |
