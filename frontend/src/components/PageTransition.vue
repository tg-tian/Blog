<template>
    <transition :name="transitionName" :mode="mode" @before-enter="beforeEnter" @enter="enter" @after-enter="afterEnter"
        @enter-cancelled="enterCancelled" @before-leave="beforeLeave" @leave="leave" @after-leave="afterLeave"
        @leave-cancelled="leaveCancelled">
        <slot></slot>
    </transition>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRoute } from 'vue-router'

const props = defineProps({
    name: {
        type: String,
        default: 'fade'
    },
    mode: {
        type: String,
        default: 'out-in'
    }
})

const route = useRoute()
const transitionName = ref(props.name)

// 过渡钩子函数
const beforeEnter = (el) => {
    // console.log('before enter')
}

const enter = (el, done) => {
    // console.log('enter')
    done()
}

const afterEnter = (el) => {
    // console.log('after enter')
}

const enterCancelled = (el) => {
    // console.log('enter cancelled')
}

const beforeLeave = (el) => {
    // console.log('before leave')
}

const leave = (el, done) => {
    // console.log('leave')
    done()
}

const afterLeave = (el) => {
    // console.log('after leave')
}

const leaveCancelled = (el) => {
    // console.log('leave cancelled')
}
</script>

<style scoped>
/* 淡入淡出过渡 */
.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}

/* 滑动过渡 */
.slide-enter-active,
.slide-leave-active {
    transition: all 0.3s ease;
}

.slide-enter-from {
    opacity: 0;
    transform: translateX(30px);
}

.slide-leave-to {
    opacity: 0;
    transform: translateX(-30px);
}

/* 缩放过渡 */
.scale-enter-active,
.scale-leave-active {
    transition: all 0.3s ease;
}

.scale-enter-from,
.scale-leave-to {
    opacity: 0;
    transform: scale(0.95);
}

/* 上下滑动过渡 */
.slide-up-enter-active,
.slide-up-leave-active {
    transition: all 0.3s ease;
}

.slide-up-enter-from {
    opacity: 0;
    transform: translateY(30px);
}

.slide-up-leave-to {
    opacity: 0;
    transform: translateY(-30px);
}
</style>