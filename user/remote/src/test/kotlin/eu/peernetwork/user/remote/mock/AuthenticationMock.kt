package eu.peernetwork.user.remote.mock

import eu.peernetwork.core.remote.model.Status
import public.eu.peernetwork.user.remote.LoginMutation

object AuthenticationMock {
    fun login(): LoginMutation.Login {
        return LoginMutation.Login(
            status = Status.SUCCESS.value,
            ResponseCode = null,
            accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJwZWVyYXBwLmRlIiwiYXVkIjoicGVlcmFwcC5kZSIsInJvbCI6MCwidWlkIjoiM2Y4Yjc2YTYtOGViMC00ZmUyLThmNGEtOThlYjI2Yjk5OWQ5IiwiaWF0IjoxNzQyOTEzNjUyLCJleHAiOjE3NDM1MTg0NTJ9.f-xQjunLU3mecyhvHEKOkuYbvHAk5hymnPYaVa4mBH53XFiWjkV8PfhuGJSncY0FLm-GgmHmbwDyDWf2cPZLFqzDL6V7QxKodMLjuREM9WJyovh-ekkphA-rqIwsRGdRBUYStdvmf2gG56L5T3RHnalADUYcB1oqn1gNkJX8hF2_-9YHyIkPZYi8VYH2YLAXOXe-WK0dLNyYwPAMzO-a1G_hjKVWOs9I-GX2clZlgsDbhiNJrhI6u3u3MBKbdzAkBW1k8_5xxAuZrT1tuQKV3Bjzz9w5D2-Uc5hEKhLWrzQnZAkOIJcmrs-bPygMvFomYorE0GsbE5P9IGVe62tj5w",
            refreshToken = "<test-refresh-token>"
        )
    }
}
