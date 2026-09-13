`GET /api/v2/filesystem/resources/{resourceRid}/roles`

List the roles on a resource.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:filesystem-read`.

**OAuth2 scopes**: `api:filesystem-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `resourceRid` | string | 是 | The unique resource identifier (RID) of a resource.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `includeInherited` | boolean | 否 | Whether to include inherited roles on the resource. |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

## Response

**ListResourceRolesResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListResourceRolesResponse` | object | 是 | 示例: `{"data":[{"resourceRolePrincipal":{"type":"principalWithId","principalId":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","principalType":"GROUP"},"roleId":"8bf49052-dc37-4528-8bf0-b551cfb71268"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListResourceRolesResponse.data` | list<ResourceRole> | 否 | — |
| `ListResourceRolesResponse.data.ResourceRole` | object | 是 | — |
| `ListResourceRolesResponse.data.ResourceRole.resourceRolePrincipal` | union | 是 | 示例: `{"type":"principalWithId","principalId":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","principalType":"GROUP"}` |
| `ListResourceRolesResponse.data.ResourceRole.resourceRolePrincipal.principalWithId` | object | 否 | Represents a user principal or group principal with an ID. |
| `ListResourceRolesResponse.data.ResourceRole.resourceRolePrincipal.principalWithId.principalId` | string | 是 | The ID of a Foundry Group or User.<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `ListResourceRolesResponse.data.ResourceRole.resourceRolePrincipal.principalWithId.principalType` | enum | 是 | 示例: `USER` |
| `ListResourceRolesResponse.data.ResourceRole.resourceRolePrincipal.everyone` | object | 否 | A principal representing all users of the platform. |
| `ListResourceRolesResponse.data.ResourceRole.roleId` | string | 是 | The unique ID for a Role. Roles are sets of permissions that grant different levels of access to resources.<br>The default roles in Foundry are: Owner, Editor, Viewer, and Discoverer. See more about<br>[roles](/docs/foundry/security/projects-and-roles#roles) in the user documentation.<br>示例: `8bf49052-dc37-4528-8bf0-b551cfb71268` |
| `ListResourceRolesResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "resourceRolePrincipal": {
        "type": "principalWithId",
        "principalId": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
        "principalType": "GROUP"
      },
      "roleId": "8bf49052-dc37-4528-8bf0-b551cfb71268"
    }
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `ResourceNotFound` | The given Resource could not be found. |
