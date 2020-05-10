<template>
    <div class="layout">
        <app-header></app-header>
        <app-navbar></app-navbar>
        <app-main></app-main>
    </div>
</template>

<script>
import AppHeader from './AppHeader'
import AppNavbar from "./AppNavbar"
import AppMain from "./AppMain"

export default {
    components: {
        AppHeader,
        AppNavbar,
        AppMain
    }
}
</script>

<style scoped>

</style>