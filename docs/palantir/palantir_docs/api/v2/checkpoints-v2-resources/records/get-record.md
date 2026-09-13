`GET /api/v2/checkpoints/records/{recordRid}`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Retrieve a single checkpoint record by id.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:checkpoints-read`.

**OAuth2 scopes**: `api:checkpoints-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `recordRid` | string | 是 | Identifier of a checkpoint record.<br>示例: `ri.checkpoints.main.checkpoint.a1b2c3d4-e5f6-7890-abcd-ef1234567890` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**Record**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Record` | object | 是 | 示例: `{"projectRid":"ri.compass.main.folder.d4e5f6a7-b8c9-0123-defa-234567890123","delegateUserId":"0d1fe74e-2b70-4a93-9b1a-80070637788b","configRid":"ri.checkpoints.main.config.b2c3d4e5-f6a7-8901-bcde-f12345678901","organizationRid":"ri.multipass..organization.e5f6a7b8-c9d0-1234-efab-345678901234","checkpointedItems":[{"type":"checkpointedResource","rid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","resourceType":"DATASET","compassPath":{"value":"/My Project/My Dataset"},"orgMarkings":[]}],"rid":"ri.checkpoints.main.checkpoint.a1b2c3d4-e5f6-7890-abcd-ef1234567890","type":"CONTOUR_EXPORT","actingUser":{"userId":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","username":{"value":"admin"}},"createdAt":"2023-11-14T09:30:00.000Z","approvalsMetadata":{"approvalsTaskId":"e3f4a5b6-c7d8-9012-efab-123456789012","approvalsSubtaskIds":["f4a5b6c7-d8e9-0123-fabc-234567890123"]},"scope":"USER_SCOPED","interactionRid":"ri.checkpoints.main.checkpointable-interaction.c3d4e5f6-a7b8-9012-cdef-123456789012","namespaceRid":"ri.compass.main.folder.f6a7b8c9-d0e1-2345-fabc-456789012345","justification":{"type":"acknowledgementJustification","prompt":"I acknowledge this action","title":"Export Confirmation"}}` |
| `Record.rid` | string | 是 | Identifier of a checkpoint record.<br>示例: `ri.checkpoints.main.checkpoint.a1b2c3d4-e5f6-7890-abcd-ef1234567890` |
| `Record.configRid` | string | 否 | Identifier of the checkpoint configuration that produced a record.<br>示例: `ri.checkpoints.main.config.b2c3d4e5-f6a7-8901-bcde-f12345678901` |
| `Record.type` | enum | 是 | Checkpoint type identifier. See the [Checkpoints documentation](/docs/foundry/checkpoints/overview)<br>for more details.<br>示例: `CONTOUR_EXPORT` |
| `Record.scope` | enum | 是 | Indicates whether the checkpoint was scoped to a user or resource.<br>示例: `USER_SCOPED` |
| `Record.actingUser` | object | 是 | User that performed the checkpoint action.<br>示例: `{"userId":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","username":{"value":"admin"}}` |
| `Record.actingUser.userId` | string | 是 | A Foundry User ID.<br>示例: `dd05feb8-662a-445d-8f4c-ce92d46bedeb` |
| `Record.actingUser.username` | object | 是 | A string value that may be redacted for privacy reasons.<br>示例: `{"value":"My Dataset"}` |
| `Record.actingUser.username.value` | string | 否 | — |
| `Record.actingUser.username.redactionType` | enum | 否 | Indicates why a string value was redacted.<br>示例: `USER_REDACTED` |
| `Record.actingUser.organizationRid` | string | 否 | Identifier of the organization associated with a checkpoint.<br>示例: `ri.multipass..organization.e5f6a7b8-c9d0-1234-efab-345678901234` |
| `Record.delegateUserId` | string | 否 | A Foundry User ID.<br>示例: `dd05feb8-662a-445d-8f4c-ce92d46bedeb` |
| `Record.createdAt` | string | 是 | The time at which the checkpoint record was created.<br>示例: `2023-11-14T09:30:00.000Z` |
| `Record.checkpointedItems` | list<CheckpointedItem> | 否 | — |
| `Record.checkpointedItems.CheckpointedItem` | union | 是 | Snapshot of the entity that was captured in a checkpoint. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedIssue` | object | 否 | An issue that was captured as part of a checkpoint. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedIssue.issueRid` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedJob` | object | 否 | A build job that was captured as part of a checkpoint. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedJob.jobRid` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedSchedule` | object | 否 | A schedule that was captured as part of a checkpoint. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedSchedule.scheduleRid` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedResource` | object | 否 | A Foundry resource that was captured as part of a checkpoint. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedResource.rid` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedResource.resourceType` | enum | 是 | Type of resource that was captured.<br>示例: `DATASET` |
| `Record.checkpointedItems.CheckpointedItem.checkpointedResource.name` | object | 否 | A string value that may be redacted for privacy reasons.<br>示例: `{"value":"My Dataset"}` |
| `Record.checkpointedItems.CheckpointedItem.checkpointedResource.name.value` | string | 否 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedResource.name.redactionType` | enum | 否 | Indicates why a string value was redacted.<br>示例: `USER_REDACTED` |
| `Record.checkpointedItems.CheckpointedItem.checkpointedResource.projectRid` | string | 否 | Identifier of the project that scoped a checkpoint.<br>示例: `ri.compass.main.folder.d4e5f6a7-b8c9-0123-defa-234567890123` |
| `Record.checkpointedItems.CheckpointedItem.checkpointedResource.namespaceRid` | string | 否 | Identifier of the namespace associated with a checkpoint.<br>示例: `ri.compass.main.folder.f6a7b8c9-d0e1-2345-fabc-456789012345` |
| `Record.checkpointedItems.CheckpointedItem.checkpointedResource.compassPath` | object | 是 | A string value that may be redacted for privacy reasons.<br>示例: `{"value":"My Dataset"}` |
| `Record.checkpointedItems.CheckpointedItem.checkpointedResource.compassPath.value` | string | 否 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedResource.compassPath.redactionType` | enum | 否 | Indicates why a string value was redacted.<br>示例: `USER_REDACTED` |
| `Record.checkpointedItems.CheckpointedItem.checkpointedResource.orgMarkings` | list<string> | 否 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedJobSpecification` | object | 否 | A job specification that was captured as part of a checkpoint. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedJobSpecification.jobSpecRid` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedLanguageModel` | object | 否 | A language model that was captured as part of a checkpoint. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedLanguageModel.modelRid` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedGroup` | object | 否 | A group that was captured as part of a checkpoint. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedGroup.groupId` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedUserIntakeSubmission` | object | 否 | A user intake form submission that was captured as part of a checkpoint. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedUserIntakeSubmission.submissionRid` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedObjectSet` | object | 否 | Represents the object set that was checkpointed. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedObjectSet.versioned` | object | 否 | A versioned object set that was captured as part of a checkpoint.<br>示例: `{"versionedObjectSetRid":"ri.object-set.main.versioned-object-set.d2e3f4a5-b6c7-8901-defa-012345678901","objectSetVersion":"e3f4a5b6-c7d8-9012-efab-123456789012","objectTypes":[]}` |
| `Record.checkpointedItems.CheckpointedItem.checkpointedObjectSet.versioned.versionedObjectSetRid` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedObjectSet.versioned.objectSetVersion` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedObjectSet.versioned.objectTypes` | list<CheckpointedOntologyWithObjectTypes> | 否 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedObjectSet.versioned.objectTypes.CheckpointedOntologyWithObjectTypes` | object | 是 | An ontology with its associated object types that was captured as part of a checkpoint. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedObjectSet.versioned.objectTypes.CheckpointedOntologyWithObjectTypes.ontology` | object | 是 | An ontology snapshot that was captured as part of a checkpoint.<br>示例: `{"ontologyRid":"ri.ontology.main.ontology.b0c1d2e3-f4a5-6789-bcde-890123456789","ontologyVersion":"c1d2e3f4-a5b6-7890-cdef-901234567890"}` |
| `Record.checkpointedItems.CheckpointedItem.checkpointedObjectSet.versioned.objectTypes.CheckpointedOntologyWithObjectTypes.ontology.ontologyRid` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedObjectSet.versioned.objectTypes.CheckpointedOntologyWithObjectTypes.ontology.ontologyVersion` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedObjectSet.versioned.objectTypes.CheckpointedOntologyWithObjectTypes.ontology.namespaceRid` | string | 否 | Identifier of the namespace associated with a checkpoint.<br>示例: `ri.compass.main.folder.f6a7b8c9-d0e1-2345-fabc-456789012345` |
| `Record.checkpointedItems.CheckpointedItem.checkpointedObjectSet.versioned.objectTypes.CheckpointedOntologyWithObjectTypes.objectTypeRids` | list<rid> | 否 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedObjectSet.typesProxy` | object | 否 | A types proxy object set that was captured as part of a checkpoint.<br>示例: `{"objectTypes":[]}` |
| `Record.checkpointedItems.CheckpointedItem.checkpointedObjectSet.typesProxy.objectTypes` | list<CheckpointedOntologyWithObjectTypes> | 否 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedObjectSet.typesProxy.objectTypes.CheckpointedOntologyWithObjectTypes` | object | 是 | An ontology with its associated object types that was captured as part of a checkpoint. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedObjectSet.typesProxy.objectTypes.CheckpointedOntologyWithObjectTypes.ontology` | object | 是 | An ontology snapshot that was captured as part of a checkpoint.<br>示例: `{"ontologyRid":"ri.ontology.main.ontology.b0c1d2e3-f4a5-6789-bcde-890123456789","ontologyVersion":"c1d2e3f4-a5b6-7890-cdef-901234567890"}` |
| `Record.checkpointedItems.CheckpointedItem.checkpointedObjectSet.typesProxy.objectTypes.CheckpointedOntologyWithObjectTypes.ontology.ontologyRid` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedObjectSet.typesProxy.objectTypes.CheckpointedOntologyWithObjectTypes.ontology.ontologyVersion` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedObjectSet.typesProxy.objectTypes.CheckpointedOntologyWithObjectTypes.ontology.namespaceRid` | string | 否 | Identifier of the namespace associated with a checkpoint.<br>示例: `ri.compass.main.folder.f6a7b8c9-d0e1-2345-fabc-456789012345` |
| `Record.checkpointedItems.CheckpointedItem.checkpointedObjectSet.typesProxy.objectTypes.CheckpointedOntologyWithObjectTypes.objectTypeRids` | list<rid> | 否 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedMarking` | object | 否 | A marking that was captured as part of a checkpoint. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedMarking.markingId` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedMarketplaceProduct` | object | 否 | A Marketplace product that was captured as part of a checkpoint. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedMarketplaceProduct.productId` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedPeeringJob` | object | 否 | A peering job that was captured as part of a checkpoint. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedPeeringJob.jobId` | string | 是 | Identifier of the peering job. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedPeeringJob.relationshipRid` | string | 是 | Resource identifier of the peering relationship. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedRole` | object | 否 | A role that was captured as part of a checkpoint. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedRole.roleId` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedIntervention` | object | 否 | An intervention that was captured as part of a checkpoint. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedIntervention.interventionRid` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedLanguageModelSession` | object | 否 | A language model session that was captured as part of a checkpoint. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedLanguageModelSession.sessionRid` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedToken` | object | 否 | An authentication token that was captured as part of a checkpoint. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedToken.tokenId` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedToken.tokenType` | enum | 是 | The type of token that was captured as part of a checkpoint.<br>示例: `USER_TOKEN` |
| `Record.checkpointedItems.CheckpointedItem.checkpointedUserIntakeFormInput` | object | 否 | A user intake form input that was captured as part of a checkpoint. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedUserIntakeFormInput.inputId` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedPrincipal` | object | 否 | A user or group principal that was captured as part of a checkpoint. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedPrincipal.id` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedPrincipal.username` | object | 是 | A string value that may be redacted for privacy reasons.<br>示例: `{"value":"My Dataset"}` |
| `Record.checkpointedItems.CheckpointedItem.checkpointedPrincipal.username.value` | string | 否 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedPrincipal.username.redactionType` | enum | 否 | Indicates why a string value was redacted.<br>示例: `USER_REDACTED` |
| `Record.checkpointedItems.CheckpointedItem.checkpointedPrincipal.organizationRid` | string | 否 | Identifier of the organization associated with a checkpoint.<br>示例: `ri.multipass..organization.e5f6a7b8-c9d0-1234-efab-345678901234` |
| `Record.checkpointedItems.CheckpointedItem.checkpointedPrincipal.role` | enum | 是 | Role the principal had relative to the checkpointed entity.<br>示例: `GROUP_MEMBER` |
| `Record.checkpointedItems.CheckpointedItem.checkpointedActionType` | object | 否 | An ontology action type that was captured as part of a checkpoint. |
| `Record.checkpointedItems.CheckpointedItem.checkpointedActionType.actionTypeRid` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedActionType.ontology` | object | 是 | An ontology snapshot that was captured as part of a checkpoint.<br>示例: `{"ontologyRid":"ri.ontology.main.ontology.b0c1d2e3-f4a5-6789-bcde-890123456789","ontologyVersion":"c1d2e3f4-a5b6-7890-cdef-901234567890"}` |
| `Record.checkpointedItems.CheckpointedItem.checkpointedActionType.ontology.ontologyRid` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedActionType.ontology.ontologyVersion` | string | 是 | — |
| `Record.checkpointedItems.CheckpointedItem.checkpointedActionType.ontology.namespaceRid` | string | 否 | Identifier of the namespace associated with a checkpoint.<br>示例: `ri.compass.main.folder.f6a7b8c9-d0e1-2345-fabc-456789012345` |
| `Record.justification` | union | 是 | Justification submitted by the user to pass a checkpoint.<br>示例: `{"type":"acknowledgementJustification","prompt":"I acknowledge this action","title":"Export Confirmation"}` |
| `Record.justification.responseJustification` | object | 否 | Checkpoint justification that requires the user to input a free-text response. |
| `Record.justification.responseJustification.response` | string | 是 | User-submitted free-text justification. |
| `Record.justification.responseJustification.prompt` | string | 是 | Prompt to which the user responds. |
| `Record.justification.responseJustification.description` | string | 否 | Supplemental information that helps users understand the prompt. |
| `Record.justification.responseJustification.title` | string | 是 | Title of the checkpoint to which the user is responding. |
| `Record.justification.dropdownJustification` | object | 否 | Checkpoint justification where the user selects one or more options from a dropdown. |
| `Record.justification.dropdownJustification.selectedOptions` | list<DropdownSelection> | 否 | Options the user selected in the dropdown. |
| `Record.justification.dropdownJustification.selectedOptions.DropdownSelection` | object | 是 | A selection made within a multi-select dropdown justification. |
| `Record.justification.dropdownJustification.selectedOptions.DropdownSelection.selectedOption` | string | 是 | Dropdown option the user selected. |
| `Record.justification.dropdownJustification.selectedOptions.DropdownSelection.additionalResponse` | string | 否 | Extra free-text response submitted alongside the dropdown selection. |
| `Record.justification.dropdownJustification.prompt` | string | 是 | Prompt to which the user-selected options respond. |
| `Record.justification.dropdownJustification.description` | string | 否 | Supplemental information that helps users understand the prompt. |
| `Record.justification.dropdownJustification.title` | string | 是 | Title of the checkpoint to which the user is responding. |
| `Record.justification.reauthenticationJustification` | object | 否 | Checkpoint justification that requires the user to reauthenticate with the platform. |
| `Record.justification.reauthenticationJustification.reauthenticationId` | string | 是 | Identifier for the reauthentication instance. |
| `Record.justification.reauthenticationJustification.prompt` | string | 是 | Prompt shown to the user during reauthentication. |
| `Record.justification.reauthenticationJustification.description` | string | 否 | Supplemental information that helps users understand the prompt. |
| `Record.justification.reauthenticationJustification.title` | string | 是 | Title of the checkpoint that the user is acknowledging. |
| `Record.justification.acknowledgementJustification` | object | 否 | Checkpoint justification that requires the user to mark a checkbox. |
| `Record.justification.acknowledgementJustification.prompt` | string | 是 | Prompt acknowledged by the user. |
| `Record.justification.acknowledgementJustification.description` | string | 否 | Supplemental information that helps users understand the prompt. |
| `Record.justification.acknowledgementJustification.title` | string | 是 | Title of the checkpoint the user is acknowledging. |
| `Record.projectRid` | string | 否 | Identifier of the project that scoped a checkpoint.<br>示例: `ri.compass.main.folder.d4e5f6a7-b8c9-0123-defa-234567890123` |
| `Record.organizationRid` | string | 否 | Identifier of the organization associated with a checkpoint.<br>示例: `ri.multipass..organization.e5f6a7b8-c9d0-1234-efab-345678901234` |
| `Record.namespaceRid` | string | 否 | Identifier of the namespace associated with a checkpoint.<br>示例: `ri.compass.main.folder.f6a7b8c9-d0e1-2345-fabc-456789012345` |
| `Record.interactionRid` | string | 否 | Identifier of the interaction associated with a record.<br>示例: `ri.checkpoints.main.checkpointable-interaction.c3d4e5f6-a7b8-9012-cdef-123456789012` |
| `Record.approvalsMetadata` | object | 否 | Metadata linking a checkpoint record to an Approvals workflow.<br>示例: `{"approvalsTaskId":"e3f4a5b6-c7d8-9012-efab-123456789012","approvalsSubtaskIds":["f4a5b6c7-d8e9-0123-fabc-234567890123"]}` |
| `Record.approvalsMetadata.approvalsTaskId` | string | 是 | Identifier of an Approvals task tied to the checkpoint.<br>示例: `e3f4a5b6-c7d8-9012-efab-123456789012` |
| `Record.approvalsMetadata.approvalsSubtaskIds` | list<ApprovalsSubtaskId> | 否 | — |
| `Record.approvalsMetadata.approvalsSubtaskIds.ApprovalsSubtaskId` | string | 是 | Identifier of an Approvals subtask tied to the checkpoint. |

```json
{
  "projectRid": "ri.compass.main.folder.d4e5f6a7-b8c9-0123-defa-234567890123",
  "delegateUserId": "0d1fe74e-2b70-4a93-9b1a-80070637788b",
  "configRid": "ri.checkpoints.main.config.b2c3d4e5-f6a7-8901-bcde-f12345678901",
  "organizationRid": "ri.multipass..organization.e5f6a7b8-c9d0-1234-efab-345678901234",
  "checkpointedItems": [
    {
      "type": "checkpointedResource",
      "rid": "ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da",
      "resourceType": "DATASET",
      "compassPath": {
        "value": "/My Project/My Dataset"
      },
      "orgMarkings": []
    }
  ],
  "rid": "ri.checkpoints.main.checkpoint.a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "type": "CONTOUR_EXPORT",
  "actingUser": {
    "userId": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
    "username": {
      "value": "admin"
    }
  },
  "createdAt": "2023-11-14T09:30:00.000Z",
  "approvalsMetadata": {
    "approvalsTaskId": "e3f4a5b6-c7d8-9012-efab-123456789012",
    "approvalsSubtaskIds": [
      "f4a5b6c7-d8e9-0123-fabc-234567890123"
    ]
  },
  "scope": "USER_SCOPED",
  "interactionRid": "ri.checkpoints.main.checkpointable-interaction.c3d4e5f6-a7b8-9012-cdef-123456789012",
  "namespaceRid": "ri.compass.main.folder.f6a7b8c9-d0e1-2345-fabc-456789012345",
  "justification": {
    "type": "acknowledgementJustification",
    "prompt": "I acknowledge this action",
    "title": "Export Confirmation"
  }
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `CheckpointRecordNotFound` | The checkpoint record could not be found. |
| PERMISSION_DENIED | `CheckpointRecordPermissionDenied` | The caller does not have permission to access the checkpoint record. |
| NOT_FOUND | `RecordNotFound` | The given Record could not be found. |
