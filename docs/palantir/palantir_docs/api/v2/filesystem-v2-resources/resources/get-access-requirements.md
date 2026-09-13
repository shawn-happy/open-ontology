`GET /api/v2/filesystem/resources/{resourceRid}/getAccessRequirements`

Returns a list of access requirements a user needs in order to view a resource. Access requirements are
composed of Organizations and Markings, and can either be applied directly to the resource or inherited.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:filesystem-read`.

**OAuth2 scopes**: `api:filesystem-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `resourceRid` | string | 是 | The unique resource identifier (RID) of a resource.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |

## Response

**AccessRequirements**

Access requirements for a resource are composed of Markings and Organizations. Organizations are disjunctive,<br>while Markings are conjunctive.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `AccessRequirements` | object | 是 | Access requirements for a resource are composed of Markings and Organizations. Organizations are disjunctive,<br>while Markings are conjunctive.<br>示例: `{"markings":[{"markingId":"18212f9a-0e63-4b79-96a0-aae04df23336"}],"organizations":[{"organizationRid":"ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa","markingId":"18212f9a-0e63-4b79-96a0-aae04df23336"}]}` |
| `AccessRequirements.organizations` | list<Organization> | 否 | — |
| `AccessRequirements.organizations.Organization` | object | 是 | [Organizations](/docs/foundry/security/orgs-and-spaces/#organizations) are access requirements applied to<br>Projects that enforce strict silos between groups of users and resources. Every user is a member of only<br>one Organization, but can be a guest member of multiple Organizations. In order to meet access requirements,<br>users must be a member or guest member of at least one Organization applied to a Project.<br>Organizations are inherited via the file hierarchy and direct dependencies. |
| `AccessRequirements.organizations.Organization.markingId` | string | 是 | The ID of a security marking.<br>示例: `18212f9a-0e63-4b79-96a0-aae04df23336` |
| `AccessRequirements.organizations.Organization.organizationRid` | string | 是 | 示例: `ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa` |
| `AccessRequirements.organizations.Organization.isDirectlyApplied` | boolean | 是 | Boolean flag to indicate if the marking is directly applied to the resource, or if it's applied<br>to a parent resource and inherited by the current resource. |
| `AccessRequirements.markings` | list<Marking> | 否 | — |
| `AccessRequirements.markings.Marking` | object | 是 | [Markings](/docs/foundry/security/markings/) provide an additional level of access control for files,<br>folders, and Projects within Foundry. Markings define eligibility criteria that restrict visibility<br>and actions to users who meet those criteria. To access a resource, a user must be a member of all<br>Markings applied to a resource to access it. |
| `AccessRequirements.markings.Marking.markingId` | string | 是 | The ID of a security marking.<br>示例: `18212f9a-0e63-4b79-96a0-aae04df23336` |
| `AccessRequirements.markings.Marking.isDirectlyApplied` | boolean | 是 | Boolean flag to indicate if the marking is directly applied to the resource, or if it's applied<br>to a parent resource and inherited by the current resource. |

```json
{
  "markings": [
    {
      "markingId": "18212f9a-0e63-4b79-96a0-aae04df23336"
    }
  ],
  "organizations": [
    {
      "organizationRid": "ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa",
      "markingId": "18212f9a-0e63-4b79-96a0-aae04df23336"
    }
  ]
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `GetAccessRequirementsPermissionDenied` | Could not getAccessRequirements the Resource. |
| NOT_FOUND | `ResourceNotFound` | The given Resource could not be found. |
