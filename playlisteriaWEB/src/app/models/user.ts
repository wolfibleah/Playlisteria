export interface User {
  idUser?: number;
  username: string;
  email?: string;
  password: string;
  isAdmin?: boolean;
}

export interface SignResponse {
  token: string;
  message: string;
}

export interface UserToken {
  exp: number;
  iat: number;
  iss: string;
  sub: string;
}
