/* eslint-disable */
// this is an auto generated file. This will be overwritten

export const onCreatePlayer = /* GraphQL */ `
  subscription OnCreatePlayer(
    $filter: ModelSubscriptionPlayerFilterInput
    $owner: String
  ) {
    onCreatePlayer(filter: $filter, owner: $owner) {
      id
      gameID
      createdAt
      updatedAt
      owner
      _version
      _deleted
      _lastChangedAt
    }
  }
`
export const onUpdatePlayer = /* GraphQL */ `
  subscription OnUpdatePlayer(
    $filter: ModelSubscriptionPlayerFilterInput
    $owner: String
  ) {
    onUpdatePlayer(filter: $filter, owner: $owner) {
      id
      gameID
      createdAt
      updatedAt
      owner
      _version
      _deleted
      _lastChangedAt
    }
  }
`
export const onDeletePlayer = /* GraphQL */ `
  subscription OnDeletePlayer(
    $filter: ModelSubscriptionPlayerFilterInput
    $owner: String
  ) {
    onDeletePlayer(filter: $filter, owner: $owner) {
      id
      gameID
      createdAt
      updatedAt
      owner
      _version
      _deleted
      _lastChangedAt
    }
  }
`
export const onCreateGame = /* GraphQL */ `
  subscription OnCreateGame(
    $filter: ModelSubscriptionGameFilterInput
    $owner: String
  ) {
    onCreateGame(filter: $filter, owner: $owner) {
      id
      name
      slug
      players {
        items {
          id
          gameID
          createdAt
          updatedAt
          owner
          _version
          _deleted
          _lastChangedAt
        }
        nextToken
        startedAt
      }
      createdAt
      updatedAt
      owner
      _version
      _deleted
      _lastChangedAt
    }
  }
`
export const onUpdateGame = /* GraphQL */ `
  subscription OnUpdateGame(
    $filter: ModelSubscriptionGameFilterInput
    $owner: String
  ) {
    onUpdateGame(filter: $filter, owner: $owner) {
      id
      name
      slug
      players {
        items {
          id
          gameID
          createdAt
          updatedAt
          owner
          _version
          _deleted
          _lastChangedAt
        }
        nextToken
        startedAt
      }
      createdAt
      updatedAt
      owner
      _version
      _deleted
      _lastChangedAt
    }
  }
`
export const onDeleteGame = /* GraphQL */ `
  subscription OnDeleteGame(
    $filter: ModelSubscriptionGameFilterInput
    $owner: String
  ) {
    onDeleteGame(filter: $filter, owner: $owner) {
      id
      name
      slug
      players {
        items {
          id
          gameID
          createdAt
          updatedAt
          owner
          _version
          _deleted
          _lastChangedAt
        }
        nextToken
        startedAt
      }
      createdAt
      updatedAt
      owner
      _version
      _deleted
      _lastChangedAt
    }
  }
`
