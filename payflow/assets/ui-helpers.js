// assets/ui-helpers.js
// small helpers: mask card, expiry, cvv, validate email/password

export function formatCardNumber(value){
  return value.replace(/\D/g,'').slice(0,16).replace(/(.{4})/g,'$1 ').trim();
}

export function formatExpiry(value){
  let v = value.replace(/\D/g,'').slice(0,4);
  if(v.length>=3) return v.slice(0,2) + '/' + v.slice(2);
  if(v.length>=1 && value.includes('/')) return value; // keep
  if(v.length>=2) return v.slice(0,2) + (value.length>2 ? '/' + v.slice(2) : '/');
  return v;
}

export function formatCvv(value){
  return value.replace(/\D/g,'').slice(0,3);
}

export function isEmail(s){ return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(s); }

export function passwordStrength(p){
  // 0..100
  let score=0;
  if(p.length>=8) score+=30;
  if(/[a-z]/.test(p)) score+=15;
  if(/[A-Z]/.test(p)) score+=15;
  if(/\d/.test(p)) score+=20;
  if(/[^A-Za-z0-9]/.test(p)) score+=20;
  return Math.min(100,score);
}
