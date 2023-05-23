/* eslint-disable */
// this is an auto generated file. This will be overwritten

export const createPlayer = /* GraphQL */ `
  mutation CreatePlayer(
    $input: CreatePlayerInput!
    $condition: ModelPlayerConditionInput
  ) {
    createPlayer(input: $input, condition: $condition) {
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
export const updatePlayer = /* GraphQL */ `
  mutation UpdatePlayer(
    $input: UpdatePlayerInput!
    $condition: ModelPlayerConditionInput
  ) {
    updatePlayer(input: $input, condition: $condition) {
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
export const deletePlayer = /* GraphQL */ `
  mutation DeletePlayer(
    $input: DeletePlayerInput!
    $condition: ModelPlayerConditionInput
  ) {
    deletePlayer(input: $input, condition: $condition) {
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
export const createGame = /* GraphQL */ `
  mutation CreateGame(
    $input: CreateGameInput!
    $condition: ModelGameConditionInput
  ) {
    createGame(input: $input, condition: $condition) {
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
export const updateGame = /* GraphQL */ `
  mutation UpdateGame(
    $input: UpdateGameInput!
    $condition: ModelGameConditionInput
  ) {
    updateGame(input: $input, condition: $condition) {
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
export const deleteGame = /* GraphQL */ `
  mutation DeleteGame(
    $input: DeleteGameInput!
    $condition: ModelGameConditionInput
  ) {
    deleteGame(input: $input, condition: $condition) {
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
