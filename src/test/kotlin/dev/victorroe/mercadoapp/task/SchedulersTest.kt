package dev.victorroe.mercadoapp.task

import dev.victorroe.mercadoapp.repository.ShoppingListRepository
import dev.victorroe.mercadoapp.service.RefreshTokenService
import dev.victorroe.mercadoapp.service.TokenDenylistService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class SchedulersTest {

    @Mock lateinit var refreshTokenService: RefreshTokenService
    @Mock lateinit var tokenDenylistService: TokenDenylistService
    @Mock lateinit var shoppingListRepository: ShoppingListRepository

    @Test
    fun `refresh token cleanup purges expired tokens`() {
        given(refreshTokenService.purgeExpired()).willReturn(3)

        RefreshTokenCleanupScheduler(refreshTokenService).purgeExpiredTokens()

        then(refreshTokenService).should().purgeExpired()
    }

    @Test
    fun `revoked token cleanup purges expired tokens`() {
        given(tokenDenylistService.purgeExpired()).willReturn(2)

        RevokedTokenCleanupScheduler(tokenDenylistService).purgeExpiredTokens()

        then(tokenDenylistService).should().purgeExpired()
    }

    @Test
    fun `weekly expiration marks old lists as expired`() {
        WeeklyExpirationScheduler(shoppingListRepository).expiredAllListEveryMonday()

        then(shoppingListRepository).should().updateExpiredLists(any(LocalDateTime::class.java) ?: LocalDateTime.MIN)
    }
}
