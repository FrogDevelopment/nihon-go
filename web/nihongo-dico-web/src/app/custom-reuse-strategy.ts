// cf https://www.softwarearchitekt.at/post/2016/12/02/sticky-routes-in-angular-2-3-with-routereusestrategy.aspx
// cf https://github.com/manfredsteyer/angular-2-reuse-strategy-sample/blob/master/app/shared/router/custom-reuse-strategy.ts
// This impl. bases upon one that can be found in the router's test cases.
import {ActivatedRouteSnapshot, DetachedRouteHandle, RouteReuseStrategy} from '@angular/router';


export class CustomReuseStrategy implements RouteReuseStrategy {

  handlers: { [key: string]: DetachedRouteHandle } = {};

  calcKey(route: ActivatedRouteSnapshot) {
    let next = route;
    let url = '';
    while (next) {
      if (next.url) {
        url = next.url.join('/');
      }
      next = next.firstChild;
    }
    // console.debug('url', url);
    return url;
  }

  shouldDetach(route: ActivatedRouteSnapshot): boolean {
    // console.debug('CustomReuseStrategy:shouldDetach', route);
    return true;
  }

  store(route: ActivatedRouteSnapshot, handle: DetachedRouteHandle): void {
    // console.debug('CustomReuseStrategy:store', route, handle);
    this.handlers[this.calcKey(route)] = handle;

  }

  shouldAttach(route: ActivatedRouteSnapshot): boolean {
    // console.debug('CustomReuseStrategy:shouldAttach', route);
    return !!route.routeConfig && !!this.handlers[this.calcKey(route)];
  }

  retrieve(route: ActivatedRouteSnapshot): DetachedRouteHandle {
    // console.debug('CustomReuseStrategy:retrieve', route);
    if (!route.routeConfig) {
      return null;
    }
    return this.handlers[this.calcKey(route)];
  }

  shouldReuseRoute(future: ActivatedRouteSnapshot, curr: ActivatedRouteSnapshot): boolean {
    // console.debug('CustomReuseStrategy:shouldReuseRoute', future, curr);
    return this.calcKey(curr) === this.calcKey(future);
  }

}
