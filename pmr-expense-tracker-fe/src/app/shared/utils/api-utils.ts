import { HttpParams } from '@angular/common/http';

export function buildHttpParams(
  entries?: Record<string, string | number | boolean | undefined | null>
): HttpParams {
  let params = new HttpParams();
  if (!entries) return params;

  Object.entries(entries).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== '') {
      params = params.append(key, String(value));
    }
  });

  return params;
}


