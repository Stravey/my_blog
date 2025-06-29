/******/ (() => { // webpackBootstrap
/******/ 	"use strict";
/******/ 	var __webpack_modules__ = ({

/***/ "./src/js/build-html.js":
/*!******************************!*\
  !*** ./src/js/build-html.js ***!
  \******************************/
/***/ ((__unused_webpack___webpack_module__, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   "default": () => (/* export default binding */ __WEBPACK_DEFAULT_EXPORT__)
/* harmony export */ });
/**
 * This file is responsible for building the DOM and updating DOM state.
 *
 * @author Tim Scanlin
 */

/* harmony default export */ function __WEBPACK_DEFAULT_EXPORT__(options) {
  const forEach = [].forEach
  const some = [].some
  const body = typeof window !== "undefined" && document.body
  const SPACE_CHAR = " "
  let tocElement
  let currentlyHighlighting = true
  let eventCount = 0

  /**
   * Create link and list elements.
   * @param {Object} d
   * @param {HTMLElement} container
   * @return {HTMLElement}
   */
  function createEl(d, container) {
    const link = container.appendChild(createLink(d))
    if (d.children.length) {
      const list = createList(d.isCollapsed)
      d.children.forEach((child) => {
        createEl(child, list)
      })
      link.appendChild(list)
    }
  }

  /**
   * Render nested heading array data into a given element.
   * @param {HTMLElement} parent Optional. If provided updates the {@see tocElement} to match.
   * @param {Array} data
   * @return {HTMLElement}
   */
  function render(parent, data) {
    const collapsed = false
    const container = createList(collapsed)

    data.forEach((d) => {
      createEl(d, container)
    })

    // Return if no TOC element is provided or known.
    tocElement = parent || tocElement
    if (tocElement === null) {
      return
    }

    // Remove existing child if it exists.
    if (tocElement.firstChild) {
      tocElement.removeChild(tocElement.firstChild)
    }

    // Just return the parent and don't append the list if no links are found.
    if (data.length === 0) {
      return tocElement
    }

    // Append the Elements that have been created
    return tocElement.appendChild(container)
  }

  /**
   * Create link element.
   * @param {Object} data
   * @return {HTMLElement}
   */
  function createLink(data) {
    const item = document.createElement("li")
    const a = document.createElement("a")
    if (options.listItemClass) {
      item.setAttribute("class", options.listItemClass)
    }

    if (options.onClick) {
      a.onclick = options.onClick
    }

    if (options.includeTitleTags) {
      a.setAttribute("title", data.textContent)
    }

    if (options.includeHtml && data.childNodes.length) {
      forEach.call(data.childNodes, (node) => {
        a.appendChild(node.cloneNode(true))
      })
    } else {
      // Default behavior. Set to textContent to keep tests happy.
      a.textContent = data.textContent
    }
    a.setAttribute("href", `${options.basePath}#${data.id}`)
    a.setAttribute(
      "class",
      `${
        options.linkClass + SPACE_CHAR
      }node-name--${data.nodeName}${SPACE_CHAR}${options.extraLinkClasses}`,
    )
    item.appendChild(a)
    return item
  }

  /**
   * Create list element.
   * @param {Boolean} isCollapsed
   * @return {HTMLElement}
   */
  function createList(isCollapsed) {
    const listElement = options.orderedList ? "ol" : "ul"
    const list = document.createElement(listElement)
    let classes = options.listClass + SPACE_CHAR + options.extraListClasses
    if (isCollapsed) {
      // No plus/equals here fixes compilation issue.
      classes = classes + SPACE_CHAR + options.collapsibleClass
      classes = classes + SPACE_CHAR + options.isCollapsedClass
    }
    list.setAttribute("class", classes)
    return list
  }

  /**
   * Update fixed sidebar class.
   * @return {HTMLElement}
   */
  function updateFixedSidebarClass() {
    const scrollTop = getScrollTop()

    const posFixedEl = document.querySelector(options.positionFixedSelector)
    if (options.fixedSidebarOffset === "auto") {
      options.fixedSidebarOffset = tocElement.offsetTop
    }

    if (scrollTop > options.fixedSidebarOffset) {
      if (posFixedEl.className.indexOf(options.positionFixedClass) === -1) {
        posFixedEl.className += SPACE_CHAR + options.positionFixedClass
      }
    } else {
      posFixedEl.className = posFixedEl.className.replace(
        SPACE_CHAR + options.positionFixedClass,
        "",
      )
    }
  }

  /**
   * Get top position of heading
   * @param {HTMLElement} obj
   * @return {int} position
   */
  function getHeadingTopPos(obj) {
    let position = 0
    if (obj !== null) {
      position = obj.offsetTop
      if (options.hasInnerContainers) {
        position += getHeadingTopPos(obj.offsetParent)
      }
    }
    return position
  }

  /**
   * Update className only when changed.
   * @param {HTMLElement} obj
   * @param {string} className
   * @return {HTMLElement} obj
   */
  function updateClassname(obj, className) {
    if (obj && obj.className !== className) {
      obj.className = className
    }
    return obj
  }

  /**
   * Update TOC highlighting and collapsed groupings.
   */
  function updateToc(headingsArray, event) {
    // Add fixed class at offset
    if (options.positionFixedSelector) {
      updateFixedSidebarClass()
    }
    // Get the top most heading currently visible on the page so we know what to highlight.
    const headings = headingsArray
    // This is needed for scroll events since document doesn't have getAttribute
    const clickedHref = event?.target?.getAttribute
      ? event?.target?.getAttribute("href")
      : null
    const isBottomMode =
      clickedHref && clickedHref.charAt(0) === "#"
        ? getIsHeaderBottomMode(clickedHref.replace("#", ""))
        : false
    const shouldUpdate = currentlyHighlighting || isBottomMode
    if (event && eventCount < 5) {
      eventCount++
    }

    if (shouldUpdate && !!tocElement && headings.length > 0) {
      const topHeader = getTopHeader(headings)

      const oldActiveTocLink = tocElement.querySelector(
        `.${options.activeLinkClass}`,
      )

      const topHeaderId = topHeader.id.replace(
        /([ #;&,.+*~':"!^$[\]()=>|/\\@])/g,
        "\\$1",
      )
      const hashId = window.location.hash.replace("#", "")
      let activeId = topHeaderId

      // Handle case where they clicked a link that cannot be scrolled to.
      const isPageBottomMode = getIsPageBottomMode()
      if (clickedHref && isBottomMode) {
        activeId = clickedHref.replace("#", "")
      } else if (
        hashId &&
        hashId !== topHeaderId &&
        isPageBottomMode &&
        (getIsHeaderBottomMode(topHeaderId) || eventCount <= 2)
      ) {
        // This is meant to handle the case
        // of showing the items as highlighted when they
        // are in bottom mode and cannot be scrolled to.
        // Make sure that they stay highlighted on refresh
        // too, not just when clicked.
        activeId = hashId
      }

      const activeTocLink = tocElement.querySelector(
        `.${options.linkClass}[href="${options.basePath}#${activeId}"]`,
      )
      // Performance improvement to only change the classes
      // for the toc if a new link should be highlighted.
      if (oldActiveTocLink === activeTocLink) {
        return
      }

      // Remove the active class from the other tocLinks.
      const tocLinks = tocElement.querySelectorAll(`.${options.linkClass}`)
      forEach.call(tocLinks, (tocLink) => {
        updateClassname(
          tocLink,
          tocLink.className.replace(SPACE_CHAR + options.activeLinkClass, ""),
        )
      })
      const tocLis = tocElement.querySelectorAll(`.${options.listItemClass}`)
      forEach.call(tocLis, (tocLi) => {
        updateClassname(
          tocLi,
          tocLi.className.replace(SPACE_CHAR + options.activeListItemClass, ""),
        )
      })

      // Add the active class to the active tocLink.
      if (
        activeTocLink &&
        activeTocLink.className.indexOf(options.activeLinkClass) === -1
      ) {
        activeTocLink.className += SPACE_CHAR + options.activeLinkClass
      }
      const li = activeTocLink?.parentNode
      if (li && li.className.indexOf(options.activeListItemClass) === -1) {
        li.className += SPACE_CHAR + options.activeListItemClass
      }

      const tocLists = tocElement.querySelectorAll(
        `.${options.listClass}.${options.collapsibleClass}`,
      )

      // Collapse the other collapsible lists.
      forEach.call(tocLists, (list) => {
        if (list.className.indexOf(options.isCollapsedClass) === -1) {
          list.className += SPACE_CHAR + options.isCollapsedClass
        }
      })

      // Expand the active link's collapsible list and its sibling if applicable.
      if (
        activeTocLink?.nextSibling &&
        activeTocLink.nextSibling.className.indexOf(
          options.isCollapsedClass,
        ) !== -1
      ) {
        updateClassname(
          activeTocLink.nextSibling,
          activeTocLink.nextSibling.className.replace(
            SPACE_CHAR + options.isCollapsedClass,
            "",
          ),
        )
      }
      removeCollapsedFromParents(activeTocLink?.parentNode.parentNode)
    }
  }

  /**
   * Remove collapsed class from parent elements.
   * @param {HTMLElement} element
   * @return {HTMLElement}
   */
  function removeCollapsedFromParents(element) {
    if (
      element &&
      element.className.indexOf(options.collapsibleClass) !== -1 &&
      element.className.indexOf(options.isCollapsedClass) !== -1
    ) {
      updateClassname(
        element,
        element.className.replace(SPACE_CHAR + options.isCollapsedClass, ""),
      )
      return removeCollapsedFromParents(element.parentNode.parentNode)
    }
    return element
  }

  /**
   * Disable TOC Animation when a link is clicked.
   * @param {Event} event
   */
  function disableTocAnimation(event) {
    const target = event.target || event.srcElement
    if (
      typeof target.className !== "string" ||
      target.className.indexOf(options.linkClass) === -1
    ) {
      return
    }
    // Bind to tocLink clicks to temporarily disable highlighting
    // while smoothScroll is animating.
    currentlyHighlighting = false
  }

  /**
   * Enable TOC Animation.
   */
  function enableTocAnimation() {
    currentlyHighlighting = true
  }

  /**
   * Return currently highlighting status.
   */
  function getCurrentlyHighlighting() {
    return currentlyHighlighting
  }

  function getIsHeaderBottomMode(headerId) {
    const scrollEl = getScrollEl()
    const activeHeading = document?.getElementById(headerId)
    const isBottomMode =
      activeHeading.offsetTop >
      scrollEl.offsetHeight -
        scrollEl.clientHeight * 1.4 -
        options.bottomModeThreshold
    return isBottomMode
  }

  function getIsPageBottomMode() {
    const scrollEl = getScrollEl()
    const isScrollable = scrollEl.scrollHeight > scrollEl.clientHeight
    const isBottomMode =
      getScrollTop() + scrollEl.clientHeight >
      scrollEl.offsetHeight - options.bottomModeThreshold
    return isScrollable && isBottomMode
  }

  function getScrollEl() {
    let el
    if (
      options.scrollContainer &&
      document.querySelector(options.scrollContainer)
    ) {
      el = document.querySelector(options.scrollContainer)
    } else {
      el = document.documentElement || body
    }
    return el
  }

  function getScrollTop() {
    const el = getScrollEl()
    return el?.scrollTop || 0
  }

  function getTopHeader(headings, scrollTop = getScrollTop()) {
    let topHeader
    some.call(headings, (heading, i) => {
      if (getHeadingTopPos(heading) > scrollTop + options.headingsOffset + 10) {
        // Don't allow negative index value.
        const index = i === 0 ? i : i - 1
        topHeader = headings[index]
        return true
      }
      if (i === headings.length - 1) {
        // This allows scrolling for the last heading on the page.
        topHeader = headings[headings.length - 1]
        return true
      }
    })
    return topHeader
  }

  function updateUrlHashForHeader(headingsArray) {
    const scrollTop = getScrollTop()
    const topHeader = getTopHeader(headingsArray, scrollTop)
    const isPageBottomMode = getIsPageBottomMode()
    if ((!topHeader || scrollTop < 5) && !isPageBottomMode) {
      if (!(window.location.hash === "#" || window.location.hash === "")) {
        window.history.pushState(null, null, "#")
      }
    } else if (topHeader && !isPageBottomMode) {
      const newHash = `#${topHeader.id}`
      if (window.location.hash !== newHash) {
        window.history.pushState(null, null, newHash)
      }
    }
  }

  return {
    enableTocAnimation,
    disableTocAnimation,
    render,
    updateToc,
    getCurrentlyHighlighting,
    getTopHeader,
    getScrollTop,
    updateUrlHashForHeader,
  }
}


/***/ }),

/***/ "./src/js/default-options.js":
/*!***********************************!*\
  !*** ./src/js/default-options.js ***!
  \***********************************/
/***/ ((__unused_webpack___webpack_module__, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   "default": () => (__WEBPACK_DEFAULT_EXPORT__)
/* harmony export */ });
/* harmony default export */ const __WEBPACK_DEFAULT_EXPORT__ = ({
  // Where to render the table of contents.
  tocSelector: ".js-toc",
  // Or, you can pass in a DOM node instead
  tocElement: null,
  // Where to grab the headings to build the table of contents.
  contentSelector: ".js-toc-content",
  // Or, you can pass in a DOM node instead
  contentElement: null,
  // Which headings to grab inside of the contentSelector element.
  headingSelector: "h1, h2, h3",
  // Headings that match the ignoreSelector will be skipped.
  ignoreSelector: ".js-toc-ignore",
  // For headings inside relative or absolute positioned
  // containers within content.
  hasInnerContainers: false,
  // Main class to add to links.
  linkClass: "toc-link",
  // Extra classes to add to links.
  extraLinkClasses: "",
  // Class to add to active links,
  // the link corresponding to the top most heading on the page.
  activeLinkClass: "is-active-link",
  // Main class to add to lists.
  listClass: "toc-list",
  // Extra classes to add to lists.
  extraListClasses: "",
  // Class that gets added when a list should be collapsed.
  isCollapsedClass: "is-collapsed",
  // Class that gets added when a list should be able
  // to be collapsed but isn't necessarily collapsed.
  collapsibleClass: "is-collapsible",
  // Class to add to list items.
  listItemClass: "toc-list-item",
  // Class to add to active list items.
  activeListItemClass: "is-active-li",
  // How many heading levels should not be collapsed.
  // For example, number 6 will show everything since
  // there are only 6 heading levels and number 0 will collapse them all.
  // The sections that are hidden will open
  // and close as you scroll to headings within them.
  collapseDepth: 0,
  // Smooth scrolling enabled.
  scrollSmooth: true,
  // Smooth scroll duration.
  scrollSmoothDuration: 420,
  // Smooth scroll offset.
  scrollSmoothOffset: 0,
  // Callback for scroll end.
  scrollEndCallback: function (e) {},
  // Headings offset between the headings and the top of
  // the document (this is meant for minor adjustments).
  headingsOffset: 1,
  // Enable the URL hash to update with the proper heading ID as
  // a user scrolls the page.
  enableUrlHashUpdateOnScroll: false,
  // type of scroll handler to use. to make scroll event not too rapid.
  // Options are: "debounce" or "throttle"
  // when set auto , use debounce less than 333ms , other use throttle.
  // for ios browser can't use history.pushState() more than 100 times per 30 seconds reason
  scrollHandlerType: "auto",
  //  scrollHandler delay in ms.
  scrollHandlerTimeout: 50,
  // Timeout between events firing to make sure it's
  // not too rapid (for performance reasons).
  throttleTimeout: 50,
  // Element to add the positionFixedClass to.
  positionFixedSelector: null,
  // Fixed position class to add to make sidebar fixed after scrolling
  // down past the fixedSidebarOffset.
  positionFixedClass: "is-position-fixed",
  // fixedSidebarOffset can be any number but by default is set
  // to auto which sets the fixedSidebarOffset to the sidebar
  // element's offsetTop from the top of the document on init.
  fixedSidebarOffset: "auto",
  // includeHtml can be set to true to include the HTML markup from the
  // heading node instead of just including the innerText.
  includeHtml: false,
  // includeTitleTags automatically sets the html title tag of the link
  // to match the title. This can be useful for SEO purposes or
  // when truncating titles.
  includeTitleTags: false,
  // onclick function to apply to all links in toc. will be called with
  // the event as the first parameter, and this can be used to stop,
  // propagation, prevent default or perform action
  onClick: function (e) {},
  // orderedList can be set to false to generate unordered lists (ul)
  // instead of ordered lists (ol)
  orderedList: true,
  // If there is a fixed article scroll container, set to calculate offset.
  scrollContainer: null,
  // prevent ToC DOM rendering if it's already rendered by an external system.
  skipRendering: false,
  // Optional callback to change heading labels.
  // For example it can be used to cut down and put ellipses on multiline headings you deem too long.
  // Called each time a heading is parsed. Expects a string and returns the modified label to display.
  // Additionally, the attribute `data-heading-label` may be used on a heading to specify
  // a shorter string to be used in the TOC.
  // function (string) => string
  headingLabelCallback: false,
  // ignore headings that are hidden in DOM
  ignoreHiddenElements: false,
  // Optional callback to modify properties of parsed headings.
  // The heading element is passed in node parameter and information
  // parsed by default parser is provided in obj parameter.
  // Function has to return the same or modified obj.
  // The heading will be excluded from TOC if nothing is returned.
  // function (object, HTMLElement) => object | void
  headingObjectCallback: null,
  // Set the base path, useful if you use a `base` tag in `head`.
  basePath: "",
  // Only takes affect when `tocSelector` is scrolling,
  // keep the toc scroll position in sync with the content.
  disableTocScrollSync: false,
  // If this is null then just use `tocElement` or `tocSelector` instead
  // assuming `disableTocScrollSync` is set to false. This allows for
  // scrolling an outer element (like a nav panel w/ search) containing the toc.
  // Please pass an element, not a selector here.
  tocScrollingWrapper: null,
  // Offset for the toc scroll (top) position when scrolling the page.
  // Only effective if `disableTocScrollSync` is false.
  tocScrollOffset: 30,
  // Threshold for when bottom mode should be enabled to handle
  // highlighting links that cannot be scrolled to.
  bottomModeThreshold: 30,
});


/***/ }),

/***/ "./src/js/index-esm.js":
/*!*****************************!*\
  !*** ./src/js/index-esm.js ***!
  \*****************************/
/***/ ((__unused_webpack___webpack_module__, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   _buildHtml: () => (/* binding */ _buildHtml),
/* harmony export */   _headingsArray: () => (/* binding */ _headingsArray),
/* harmony export */   _options: () => (/* binding */ _options),
/* harmony export */   _parseContent: () => (/* binding */ _parseContent),
/* harmony export */   _scrollListener: () => (/* binding */ _scrollListener),
/* harmony export */   "default": () => (__WEBPACK_DEFAULT_EXPORT__),
/* harmony export */   destroy: () => (/* binding */ destroy),
/* harmony export */   init: () => (/* binding */ init),
/* harmony export */   refresh: () => (/* binding */ refresh)
/* harmony export */ });
/* harmony import */ var _build_html_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./build-html.js */ "./src/js/build-html.js");
/* harmony import */ var _default_options_js__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./default-options.js */ "./src/js/default-options.js");
/* harmony import */ var _parse_content_js__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./parse-content.js */ "./src/js/parse-content.js");
/* harmony import */ var _scroll_smooth_index_js__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./scroll-smooth/index.js */ "./src/js/scroll-smooth/index.js");
/* harmony import */ var _update_toc_scroll_js__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ./update-toc-scroll.js */ "./src/js/update-toc-scroll.js");
/* eslint no-var: off */
/**
 * Tocbot
 * Tocbot creates a table of contents based on HTML headings on a page,
 * this allows users to easily jump to different sections of the document.
 * Tocbot was inspired by tocify (http://gregfranko.com/jquery.tocify.js/).
 * The main differences are that it works natively without any need for jquery or jquery UI).
 *
 * @author Tim Scanlin
 */







// For testing purposes.
let _options = {} // Object to store current options.
let _buildHtml
let _parseContent
let _headingsArray
let _scrollListener

let clickListener

/**
 * Initialize tocbot.
 * @param {object} customOptions
 */
function init(customOptions) {
  // Merge defaults with user options.
  // Set to options variable at the top.
  let hasInitialized = false
  _options = extend(_default_options_js__WEBPACK_IMPORTED_MODULE_1__["default"], customOptions || {})

  // Init smooth scroll if enabled (default).
  if (_options.scrollSmooth) {
    _options.duration = _options.scrollSmoothDuration
    _options.offset = _options.scrollSmoothOffset

    ;(0,_scroll_smooth_index_js__WEBPACK_IMPORTED_MODULE_3__["default"])(_options)
  }

  // Pass options to these modules.
  _buildHtml = (0,_build_html_js__WEBPACK_IMPORTED_MODULE_0__["default"])(_options)
  _parseContent = (0,_parse_content_js__WEBPACK_IMPORTED_MODULE_2__["default"])(_options)

  // Destroy it if it exists first.
  destroy()

  const contentElement = getContentElement(_options)
  if (contentElement === null) {
    return
  }

  const tocElement = getTocElement(_options)
  if (tocElement === null) {
    return
  }

  // Get headings array.
  _headingsArray = _parseContent.selectHeadings(
    contentElement,
    _options.headingSelector,
  )

  // Return if no headings are found.
  if (_headingsArray === null) {
    return
  }

  // Build nested headings array.
  const nestedHeadingsObj = _parseContent.nestHeadingsArray(_headingsArray)
  const nestedHeadings = nestedHeadingsObj.nest

  // Render.
  if (!_options.skipRendering) {
    _buildHtml.render(tocElement, nestedHeadings)
  } else {
    // No need to attach listeners if skipRendering is true, this was causing errors.
    return this
  }

  // Update Sidebar and bind listeners.
  let isClick = false
  // choose timeout by _options
  const scrollHandlerTimeout =
    _options.scrollHandlerTimeout || _options.throttleTimeout // compatible with legacy configurations
  // choose debounce or throttle
  // default use debounce when delay is less than 333ms
  // the reason is ios browser has a limit : can't use history.pushState() more than 100 times per 30 seconds
  const scrollHandler = (fn, delay) =>
    getScrollHandler(fn, delay, _options.scrollHandlerType)

  _scrollListener = scrollHandler((e) => {
    _buildHtml.updateToc(_headingsArray, e)
    // Only do this update for normal scrolls and not during clicks.
    !_options.disableTocScrollSync && !isClick && (0,_update_toc_scroll_js__WEBPACK_IMPORTED_MODULE_4__["default"])(_options)

    if (_options.enableUrlHashUpdateOnScroll && hasInitialized) {
      const enableUpdatingHash = _buildHtml.getCurrentlyHighlighting()
      enableUpdatingHash && _buildHtml.updateUrlHashForHeader(_headingsArray)
    }

    const isTop = e?.target?.scrollingElement?.scrollTop === 0
    if ((e && (e.eventPhase === 0 || e.currentTarget === null)) || isTop) {
      _buildHtml.updateToc(_headingsArray)
      _options.scrollEndCallback?.(e)
    }
  }, scrollHandlerTimeout)
  // Fire it initially to setup the page.
  if (!hasInitialized) {
    _scrollListener()
    hasInitialized = true
  }

  // Fire scroll listener on hash change to trigger highlighting changes too.
  window.onhashchange = window.onscrollend = (e) => {
    _scrollListener(e)
  }

  if (
    _options.scrollContainer &&
    document.querySelector(_options.scrollContainer)
  ) {
    document
      .querySelector(_options.scrollContainer)
      .addEventListener("scroll", _scrollListener, false)
    document
      .querySelector(_options.scrollContainer)
      .addEventListener("resize", _scrollListener, false)
  } else {
    document.addEventListener("scroll", _scrollListener, false)
    document.addEventListener("resize", _scrollListener, false)
  }

  // Bind click listeners to disable animation.
  let timeout = null
  clickListener = throttle((event) => {
    isClick = true
    if (_options.scrollSmooth) {
      _buildHtml.disableTocAnimation(event)
    }
    _buildHtml.updateToc(_headingsArray, event)
    // Timeout to re-enable the animation.
    timeout && clearTimeout(timeout)
    timeout = setTimeout(() => {
      _buildHtml.enableTocAnimation()
    }, _options.scrollSmoothDuration)
    // Set is click w/ a bit of delay so that animations can finish
    // and we don't disturb the user while they click the toc.
    setTimeout(() => {
      isClick = false
    }, _options.scrollSmoothDuration + 100)
  }, _options.throttleTimeout)

  if (
    _options.scrollContainer &&
    document.querySelector(_options.scrollContainer)
  ) {
    document
      .querySelector(_options.scrollContainer)
      .addEventListener("click", clickListener, false)
  } else {
    document.addEventListener("click", clickListener, false)
  }
}

/**
 * Destroy tocbot.
 */
function destroy() {
  const tocElement = getTocElement(_options)
  if (tocElement === null) {
    return
  }

  if (!_options.skipRendering) {
    // Clear HTML.
    if (tocElement) {
      tocElement.innerHTML = ""
    }
  }

  // Remove event listeners.
  if (
    _options.scrollContainer &&
    document.querySelector(_options.scrollContainer)
  ) {
    document
      .querySelector(_options.scrollContainer)
      .removeEventListener("scroll", _scrollListener, false)
    document
      .querySelector(_options.scrollContainer)
      .removeEventListener("resize", _scrollListener, false)
    if (_buildHtml) {
      document
        .querySelector(_options.scrollContainer)
        .removeEventListener("click", clickListener, false)
    }
  } else {
    document.removeEventListener("scroll", _scrollListener, false)
    document.removeEventListener("resize", _scrollListener, false)
    if (_buildHtml) {
      document.removeEventListener("click", clickListener, false)
    }
  }
}

/**
 * Refresh tocbot.
 */
function refresh(customOptions) {
  destroy()
  init(customOptions || _options)
}

// From: https://github.com/Raynos/xtend
const hasOwnProp = Object.prototype.hasOwnProperty
function extend(...args) {
  const target = {}
  for (let i = 0; i < args.length; i++) {
    const source = args[i]
    for (const key in source) {
      if (hasOwnProp.call(source, key)) {
        target[key] = source[key]
      }
    }
  }
  return target
}

// From: https://remysharp.com/2010/07/21/throttling-function-calls
function throttle(fn, threshold, scope) {
  threshold || (threshold = 250)
  let last
  let deferTimer
  return function (...args) {
    const context = scope || this
    const now = +new Date()
    if (last && now < last + threshold) {
      // hold on to it
      clearTimeout(deferTimer)
      deferTimer = setTimeout(() => {
        last = now
        fn.apply(context, args)
      }, threshold)
    } else {
      last = now
      fn.apply(context, args)
    }
  }
}

/**
 * Creates a debounced function that delays invoking `func` until after `wait` milliseconds
 * have elapsed since the last time the debounced function was invoked.
 *
 * @param {Function} func - The function to debounce.
 * @param {number} wait - The number of milliseconds to delay.
 * @returns {Function} - Returns the new debounced function.
 */
function debounce(func, wait) {
  let timeout
  return (...args) => {
    clearTimeout(timeout)
    timeout = setTimeout(() => func.apply(this, args), wait)
  }
}

/**
 * Creates a scroll handler with specified timeout strategy
 * @param {number} timeout - Delay duration in milliseconds
 * @param {'debounce'|'throttle'|'auto'} type - Strategy type for scroll handling
 * @returns {Function} Configured scroll handler function
 */
function getScrollHandler(func, timeout, type = "auto") {
  switch (type) {
    case "debounce":
      return debounce(func, timeout)
    case "throttle":
      return throttle(func, timeout)
    default:
      return timeout < 334 ? debounce(func, timeout) : throttle(func, timeout)
  }
}

function getContentElement(options) {
  try {
    return (
      options.contentElement || document.querySelector(options.contentSelector)
    )
  } catch (e) {
    console.warn(`Contents element not found: ${options.contentSelector}`) // eslint-disable-line
    return null
  }
}

function getTocElement(options) {
  try {
    return options.tocElement || document.querySelector(options.tocSelector)
  } catch (e) {
    console.warn(`TOC element not found: ${options.tocSelector}`) // eslint-disable-line
    return null
  }
}

// Add default export for easier use.
const tocbot = {
  _options,
  _buildHtml,
  _parseContent,
  init,
  destroy,
  refresh,
}

/* harmony default export */ const __WEBPACK_DEFAULT_EXPORT__ = (tocbot);


/***/ }),

/***/ "./src/js/parse-content.js":
/*!*********************************!*\
  !*** ./src/js/parse-content.js ***!
  \*********************************/
/***/ ((__unused_webpack___webpack_module__, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   "default": () => (/* binding */ parseContent)
/* harmony export */ });
/**
 * This file is responsible for parsing the content from the DOM and making
 * sure data is nested properly.
 *
 * @author Tim Scanlin
 */

function parseContent(options) {
  const reduce = [].reduce

  /**
   * Get the last item in an array and return a reference to it.
   * @param {Array} array
   * @return {Object}
   */
  function getLastItem(array) {
    return array[array.length - 1]
  }

  /**
   * Get heading level for a heading dom node.
   * @param {HTMLElement} heading
   * @return {Number}
   */
  function getHeadingLevel(heading) {
    return +heading.nodeName.toUpperCase().replace("H", "")
  }

  /**
   * Determine whether the object is an HTML Element.
   * Also works inside iframes. HTML Elements might be created by the parent document.
   * @param {Object} maybeElement
   * @return {Number}
   */
  function isHTMLElement(maybeElement) {
    try {
      return (
        maybeElement instanceof window.HTMLElement ||
        maybeElement instanceof window.parent.HTMLElement
      )
    } catch (e) {
      return maybeElement instanceof window.HTMLElement
    }
  }

  /**
   * Get important properties from a heading element and store in a plain object.
   * @param {HTMLElement} heading
   * @return {Object}
   */
  function getHeadingObject(heading) {
    // each node is processed twice by this method because nestHeadingsArray() and addNode() calls it
    // first time heading is real DOM node element, second time it is obj
    // that is causing problem so I am processing only original DOM node
    if (!isHTMLElement(heading)) return heading

    if (
      options.ignoreHiddenElements &&
      (!heading.offsetHeight || !heading.offsetParent)
    ) {
      return null
    }

    const headingLabel =
      heading.getAttribute("data-heading-label") ||
      (options.headingLabelCallback
        ? String(options.headingLabelCallback(heading.innerText))
        : (heading.innerText || heading.textContent).trim())
    const obj = {
      id: heading.id,
      children: [],
      nodeName: heading.nodeName,
      headingLevel: getHeadingLevel(heading),
      textContent: headingLabel,
    }

    if (options.includeHtml) {
      obj.childNodes = heading.childNodes
    }

    if (options.headingObjectCallback) {
      return options.headingObjectCallback(obj, heading)
    }

    return obj
  }

  /**
   * Add a node to the nested array.
   * @param {Object} node
   * @param {Array} nest
   * @return {Array}
   */
  function addNode(node, nest) {
    const obj = getHeadingObject(node)
    const level = obj.headingLevel
    let array = nest
    let lastItem = getLastItem(array)
    const lastItemLevel = lastItem ? lastItem.headingLevel : 0
    let counter = level - lastItemLevel

    while (counter > 0) {
      lastItem = getLastItem(array)
      // Handle case where there are multiple h5+ in a row.
      if (lastItem && level === lastItem.headingLevel) {
        break
      } else if (lastItem && lastItem.children !== undefined) {
        array = lastItem.children
      }
      counter--
    }

    if (level >= options.collapseDepth) {
      obj.isCollapsed = true
    }

    array.push(obj)
    return array
  }

  /**
   * Select headings in content area, exclude any selector in options.ignoreSelector
   * @param {HTMLElement} contentElement
   * @param {Array} headingSelector
   * @return {Array}
   */
  function selectHeadings(contentElement, headingSelector) {
    let selectors = headingSelector
    if (options.ignoreSelector) {
      selectors = headingSelector
        .split(",")
        .map(function mapSelectors(selector) {
          return `${selector.trim()}:not(${options.ignoreSelector})`
        })
    }
    try {
      return contentElement.querySelectorAll(selectors)
    } catch (e) {
      console.warn(`Headers not found with selector: ${selectors}`) // eslint-disable-line
      return null
    }
  }

  /**
   * Nest headings array into nested arrays with 'children' property.
   * @param {Array} headingsArray
   * @return {Object}
   */
  function nestHeadingsArray(headingsArray) {
    return reduce.call(
      headingsArray,
      function reducer(prev, curr) {
        const currentHeading = getHeadingObject(curr)
        if (currentHeading) {
          addNode(currentHeading, prev.nest)
        }
        return prev
      },
      {
        nest: [],
      },
    )
  }

  return {
    nestHeadingsArray,
    selectHeadings,
  }
}


/***/ }),

/***/ "./src/js/scroll-smooth/index.js":
/*!***************************************!*\
  !*** ./src/js/scroll-smooth/index.js ***!
  \***************************************/
/***/ ((__unused_webpack___webpack_module__, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   "default": () => (/* binding */ initSmoothScrolling)
/* harmony export */ });
/* eslint no-var: off */
/* globals location, requestAnimationFrame */

function initSmoothScrolling(options) {
  // if (isCssSmoothSCrollSupported()) { return }

  var duration = options.duration
  var offset = options.offset
  if (typeof window === "undefined" || typeof location === "undefined") return

  var pageUrl = location.hash ? stripHash(location.href) : location.href

  delegatedLinkHijacking()

  function delegatedLinkHijacking() {
    document.body.addEventListener("click", onClick, false)

    function onClick(e) {
      if (
        !isInPageLink(e.target) ||
        e.target.className.indexOf("no-smooth-scroll") > -1 ||
        (e.target.href.charAt(e.target.href.length - 2) === "#" &&
          e.target.href.charAt(e.target.href.length - 1) === "!") ||
        e.target.className.indexOf(options.linkClass) === -1
      ) {
        return
      }

      // Don't prevent default or hash doesn't change.
      // e.preventDefault()

      jump(e.target.hash, {
        duration,
        offset,
        callback: function () {
          setFocus(e.target.hash)
        },
      })
    }
  }

  function isInPageLink(n) {
    return (
      n.tagName.toLowerCase() === "a" &&
      (n.hash.length > 0 || n.href.charAt(n.href.length - 1) === "#") &&
      (stripHash(n.href) === pageUrl || stripHash(n.href) + "#" === pageUrl)
    )
  }

  function stripHash(url) {
    return url.slice(0, url.lastIndexOf("#"))
  }

  // function isCssSmoothSCrollSupported () {
  //   return 'scrollBehavior' in document.documentElement.style
  // }

  // Adapted from:
  // https://www.nczonline.net/blog/2013/01/15/fixing-skip-to-content-links/
  function setFocus(hash) {
    var element = document.getElementById(hash.substring(1))

    if (element) {
      if (!/^(?:a|select|input|button|textarea)$/i.test(element.tagName)) {
        element.tabIndex = -1
      }

      element.focus()
    }
  }
}

function jump(target, options) {
  var start = window.pageYOffset
  var opt = {
    duration: options.duration,
    offset: options.offset || 0,
    callback: options.callback,
    easing: options.easing || easeInOutQuad,
  }
  // This makes ids that start with a number work: ('[id="' + decodeURI(target).split('#').join('') + '"]')
  // DecodeURI for nonASCII hashes, they was encoded, but id was not encoded, it lead to not finding the tgt element by id.
  // And this is for IE: document.body.scrollTop
  // Handle decoded and non-decoded URIs since sometimes URLs automatically transform them (support for internation chars).
  var tgt =
    document.querySelector(
      '[id="' + decodeURI(target).split("#").join("") + '"]',
    ) || document.querySelector('[id="' + target.split("#").join("") + '"]')
  var distance =
    typeof target === "string"
      ? opt.offset +
        (target
          ? (tgt && tgt.getBoundingClientRect().top) || 0 // handle non-existent links better.
          : -(document.documentElement.scrollTop || document.body.scrollTop))
      : target
  var duration =
    typeof opt.duration === "function" ? opt.duration(distance) : opt.duration
  var timeStart
  var timeElapsed

  requestAnimationFrame(function (time) {
    timeStart = time
    loop(time)
  })
  function loop(time) {
    timeElapsed = time - timeStart

    window.scrollTo(0, opt.easing(timeElapsed, start, distance, duration))

    if (timeElapsed < duration) {
      requestAnimationFrame(loop)
    } else {
      end()
    }
  }

  function end() {
    window.scrollTo(0, start + distance)

    if (typeof opt.callback === "function") {
      opt.callback()
    }
  }

  // Robert Penner's easeInOutQuad - http://robertpenner.com/easing/
  function easeInOutQuad(t, b, c, d) {
    t /= d / 2
    if (t < 1) return (c / 2) * t * t + b
    t--
    return (-c / 2) * (t * (t - 2) - 1) + b
  }
}


/***/ }),

/***/ "./src/js/update-toc-scroll.js":
/*!*************************************!*\
  !*** ./src/js/update-toc-scroll.js ***!
  \*************************************/
/***/ ((__unused_webpack___webpack_module__, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   "default": () => (/* binding */ updateTocScroll)
/* harmony export */ });
function updateTocScroll(options) {
  const toc =
    options.tocScrollingWrapper ||
    options.tocElement ||
    document.querySelector(options.tocSelector)
  if (toc && toc.scrollHeight > toc.clientHeight) {
    const activeItem = toc.querySelector(`.${options.activeListItemClass}`)
    if (activeItem) {
      // Determine element top and bottom
      const eTop = activeItem.offsetTop

      // Check if out of view
      // Above scroll view
      const scrollAmount = eTop - options.tocScrollOffset
      toc.scrollTop = scrollAmount > 0 ? scrollAmount : 0
    }
  }
}


/***/ })

/******/ 	});
/************************************************************************/
/******/ 	// The module cache
/******/ 	var __webpack_module_cache__ = {};
/******/ 	
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/ 		// Check if module is in cache
/******/ 		var cachedModule = __webpack_module_cache__[moduleId];
/******/ 		if (cachedModule !== undefined) {
/******/ 			return cachedModule.exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = __webpack_module_cache__[moduleId] = {
/******/ 			// no module.id needed
/******/ 			// no module.loaded needed
/******/ 			exports: {}
/******/ 		};
/******/ 	
/******/ 		// Execute the module function
/******/ 		__webpack_modules__[moduleId](module, module.exports, __webpack_require__);
/******/ 	
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/ 	
/************************************************************************/
/******/ 	/* webpack/runtime/define property getters */
/******/ 	(() => {
/******/ 		// define getter functions for harmony exports
/******/ 		__webpack_require__.d = (exports, definition) => {
/******/ 			for(var key in definition) {
/******/ 				if(__webpack_require__.o(definition, key) && !__webpack_require__.o(exports, key)) {
/******/ 					Object.defineProperty(exports, key, { enumerable: true, get: definition[key] });
/******/ 				}
/******/ 			}
/******/ 		};
/******/ 	})();
/******/ 	
/******/ 	/* webpack/runtime/hasOwnProperty shorthand */
/******/ 	(() => {
/******/ 		__webpack_require__.o = (obj, prop) => (Object.prototype.hasOwnProperty.call(obj, prop))
/******/ 	})();
/******/ 	
/******/ 	/* webpack/runtime/make namespace object */
/******/ 	(() => {
/******/ 		// define __esModule on exports
/******/ 		__webpack_require__.r = (exports) => {
/******/ 			if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 				Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 			}
/******/ 			Object.defineProperty(exports, '__esModule', { value: true });
/******/ 		};
/******/ 	})();
/******/ 	
/************************************************************************/
var __webpack_exports__ = {};
// This entry needs to be wrapped in an IIFE because it needs to be isolated against other modules in the chunk.
(() => {
/*!******************************!*\
  !*** ./src/js/index-dist.js ***!
  \******************************/
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _index_esm_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./index-esm.js */ "./src/js/index-esm.js");
/* globals define */

((root, factory) => {
  if (typeof define === "function" && define.amd) {
    define([], factory(root))
  } else if (typeof exports === "object" && !(exports instanceof HTMLElement)) {
    module.exports = factory(root)
  } else {
    root.tocbot = factory(root)
  }
})(
  typeof global !== "undefined" && !(global instanceof HTMLElement)
    ? global
    : window || global,
  (root) => {
    // Just return if its not a browser.
    const supports =
      !!root &&
      !!root.document &&
      !!root.document.querySelector &&
      !!root.addEventListener // Feature test
    if (typeof window === "undefined" && !supports) {
      return
    }

    // Make tocbot available globally.
    root.tocbot = _index_esm_js__WEBPACK_IMPORTED_MODULE_0__

    return _index_esm_js__WEBPACK_IMPORTED_MODULE_0__
  },
)

})();

/******/ })()
;
//# sourceMappingURL=main.js.map